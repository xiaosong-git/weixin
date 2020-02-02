package com.company.project.util;

import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
public class OkHttpUtil {

	public interface OnNetworkListen {
		public void onSuccess(String networkResult);

		public void onFailer();
	}


	private static OkHttpClient okHttpClient = new OkHttpClient();



	/**
	 * post 上传文件
	 *
	 * @param url
	 * @param params
	 * @param fileType
	 * @return
	 */
	public static String postFile(String url, Map<String, Object> params, String fileType) {
		String responseBody = "";
		MultipartBody.Builder builder = new MultipartBody.Builder();
		// 添加参数
		if (params != null && params.keySet().size() > 0) {
			for (String key : params.keySet()) {
				if (params.get(key) instanceof File) {
					File file = (File) params.get(key);
					builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(fileType), file));
					continue;
				}
				builder.addFormDataPart(key, params.get(key).toString());
			}
		}
		Request request = new Request.Builder().url(url).post(builder.build()).build();
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			int status = response.code();
			if (status == 200) {
				return response.body().string();
			}
		} catch (Throwable throwable) {

        
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return responseBody;
	}

    public static void main(String[] args) {
	    Map<String,Object> map=new HashMap();
	    map.put("userId",45);
	    map.put("type",3);
        File file=new File("D:\\test2\\12345.jpg");
        map.put("file",file);
        String s = postFile("http://localhost:8082/goldccm-imgServer/goldccm/image/gainData", map, "multipart/form-data");
        System.out.println(s);
    }
}
