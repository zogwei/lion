/**
 * 版权声明：bee 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-remote
 * @Title: FastJsonSerialization.java
 * @Package com.alacoder.lion.remote.serialization
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:29:35
 * @version V1.0
 */

package com.alacoder.lion.remote.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.alacoder.lion.common.extension.SpiMeta;
import com.alacoder.lion.remote.Serialization;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

/**
 * @ClassName: FastJsonSerialization
 * @Description: TODO
 * @author jimmy.zhong
 * @date 2016年8月9日 上午11:29:35
 *
 */
@SpiMeta( name  = "hessian2")
public class Hessian2Serialization implements Serialization{

	
	@Override
	public byte[] serialize(Object obj) throws IOException {
		if( obj== null) throw new NullPointerException();  
	      
		ByteArrayOutputStream os = new ByteArrayOutputStream();  
	    HessianOutput ho = new HessianOutput(os);  
	    ho.writeObject(obj);  
	    return os.toByteArray();  
	}

	
	@SuppressWarnings("unchecked")
	public <T> T deserialize(byte[] bytes, Class<T> cls) throws IOException {
		if( bytes == null) throw new NullPointerException();  
	      
	    ByteArrayInputStream is = new ByteArrayInputStream(bytes);  
	    HessianInput hi = new HessianInput(is); 
	    return (T)hi.readObject(cls);  
	}

}
