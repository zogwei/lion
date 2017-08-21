package com.alacoder.lion.tcc.dubbo.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;

import com.alacoder.lion.tcc.Compensable;
import com.alacoder.lion.tcc.DefaultTransactionContext;
import com.alacoder.lion.tcc.DefaultTransactionManager;
import com.alacoder.lion.tcc.InvocationContext;
import com.alacoder.lion.tcc.Participant;
import com.alacoder.lion.tcc.SystemException;
import com.alacoder.lion.tcc.Transaction;
import com.alacoder.lion.tcc.TransactionContext;
import com.alacoder.lion.tcc.TransactionManager;
import com.alacoder.lion.tcc.TransactionStatus;
import com.alacoder.lion.tcc.TransactionXid;
import com.alacoder.lion.tcc.utils.ByteUtils;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TccServiceFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(TccServiceFilter.class);

    private TransactionManager  transactionManager = DefaultTransactionManager.getInstance();

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        logger.debug("TccServiceFilter begin");
        if (RpcContext.getContext().isProviderSide()) {
            return this.providerInvoke(invoker, invocation);
        } else {
            return this.consumerInvoke(invoker, invocation);
        }
    }

    public Result providerInvoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url = RpcContext.getContext().getUrl();
        TransactionContext transactionContext = null;
        String transactionContextContent = invocation.getAttachment(TransactionContext.class.getName());
        if (StringUtils.isNotEmpty(transactionContextContent)) {
            byte[] requestByteArray = ByteUtils.stringToByteArray(transactionContextContent);
            ByteArrayInputStream bais = new ByteArrayInputStream(requestByteArray);
            HessianInput input = new HessianInput(bais);
            try {
                transactionContext = (TransactionContext) input.readObject();
            } catch (IOException ex) {
                logger.error("Error occurred in remote call!", ex);
                throw new SystemException(ex.getMessage());
            }
        }


        if (transactionContext.getStatus() == TransactionStatus.TRYING.id()) {
            // 开启一个本地事务，判断事务阶段，一阶段生成事务 ，以便事务的传播
            Transaction transaction = transactionManager.getRemoteTransaction(transactionContext);
            Class targetClass = invocation.getClass();
            invoker.getInterface();
            TransactionXid xid = null;
            xid = new TransactionXid(transaction.getXid().getGlobalTransactionId());

            invocation.getArguments();

            Compensable compensable = (Compensable) targetClass.getAnnotation(Compensable.class);

            String confirmMethodName = compensable.confirmMethod();
            String cancelMethodName = compensable.cancelMethod();

            InvocationContext confirmInvocation = new InvocationContext(targetClass, confirmMethodName,
                                                                        invocation.getParameterTypes(),
                                                                        invocation.getArguments());

            InvocationContext cancelInvocation = new InvocationContext(targetClass, confirmMethodName,
                                                                       invocation.getParameterTypes(),
                                                                       invocation.getArguments());
            Participant participant = new Participant(xid, confirmInvocation, cancelInvocation);

            participant.setTargetClass(targetClass);

            transactionManager.enlistParticipant(participant);

        } else if (transactionContext.getStatus() == TransactionStatus.CANCELLING.id()) {
            // 二阶段，回滚
            // 根据全局事务id和参与分支id获得本地参与者局部tcc事务（注意一个全局事务，两个局部分支），回顾
            Transaction transaction = transactionManager.getRemoteTransaction(transactionContext);
            transaction.rollback();

        } else if (transactionContext.getStatus() == TransactionStatus.CONFIRMING.id()) {
            // 二阶段，提交
            // 根据全局事务id和参与分支id获得本地参与者局部tcc事务（注意一个全局事务，两个局部分支），提交
            Transaction transaction = transactionManager.getRemoteTransaction(transactionContext);
            transaction.commit();
        }

        return new RpcResult();
    }

    public Result consumerInvoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url = RpcContext.getContext().getUrl();
        String interfaceClazz = url.getServiceInterface();

        // 获得事务，如果事务是第一阶段，添加参与者，否则直接调用
        String methodName = invocation.getMethodName();

        String confirmMethodName = methodName;
        String cancelMethodName = methodName;

        Transaction transaction = transactionManager.getCurrentTransaction();
        if (transaction == null) {
            throw new SystemException("dubbo consumer must in tcc transaction");
        }
        TransactionXid xid = null;
        if (transaction.getTransactionStatus().equals(TransactionStatus.TRYING)) {
            xid = new TransactionXid(transaction.getXid().getGlobalTransactionId());

            Class targetClass = invocation.getClass();

            invocation.getArguments();

            InvocationContext confirmInvocation = new InvocationContext(targetClass, confirmMethodName,
                                                                        invocation.getParameterTypes(),
                                                                        invocation.getArguments());

            InvocationContext cancelInvocation = new InvocationContext(targetClass, confirmMethodName,
                                                                       invocation.getParameterTypes(),
                                                                       invocation.getArguments());
            Participant participant = new Participant(xid, confirmInvocation, cancelInvocation);

            participant.setTargetClass(targetClass);

            transactionManager.enlistParticipant(participant);
        }

        // 添加事务上下文
        DefaultTransactionContext transactionContext = new DefaultTransactionContext();
        transactionContext.setXid(xid);
        transactionContext.setStatus(transaction.getTransactionStatus().id());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HessianOutput output = new HessianOutput(baos);
        try {
            output.writeObject(transactionContext);
        } catch (IOException ex) {
            logger.error("Error occurred in remote call!", ex);
            throw new SystemException(ex.getMessage());
        }
        String transactionContextContent = ByteUtils.byteArrayToString(baos.toByteArray());
        Map<String, String> attachments = invocation.getAttachments();
        attachments.put(TransactionContext.class.getName(), transactionContextContent);

        return invoker.invoke(invocation);
    }

}
