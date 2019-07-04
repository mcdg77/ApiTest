import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;


public class Main {
	public static void main(String[] args) {
		String data="";
		String url ="";
		String method="";	
		JSONObject retval;		
		String token="";
		
		
		try {			
			System.out.println("===== KaKaoPay_PreTest API Test Start  ======\n");
			
	        /* sign up */
			System.out.println("1. Sign up");
	        data ="{    \"userid\": \"mcdg77\"," +
	        			"\"passwd\":\"k1014\"}";
	        url ="http://localhost:8080/member/signup";
	        method = "POST";                
			retval = new JSONObject(SendAPI(url, method, data,""));
			System.out.println( "Message:[" + retval.getString("Message") +"]" );
			System.out.println( "Result:[" + retval.getString("Result") + "]");        
	        
			
	        /* sign in */
	        System.out.println("\n2. Sign in");
			data ="{    \"userid\": \"mcdg77\"," +
						"\"passwd\" : \"k1014\" }";
			url ="http://localhost:8080/member/signin";
			method = "POST";           							
			retval = new JSONObject(SendAPI(url, method, data,""));
			System.out.println( "Message:[" + retval.getString("Message") +"]");
			System.out.println( "Result:[" + retval.getString("Result") +"]");
			token = retval.getString("Result");
						
			
			/* CVS Save */
	        System.out.println("\n3. CVS Save ");
			url ="http://localhost:8080/housefund/save";
			method = "GET";               							
			retval = new JSONObject(SendAPI(url, method, data, token));
			System.out.println( "Message:[" + retval.getString("Message") +"]");
			System.out.println( "Count:[" + retval.getString("Count") +"]");
			
			
			/* institute List */
	        System.out.println("\n4. Institute List in");
			url ="http://localhost:8080/housefund/inslist";
			method = "GET";               							
			retval = new JSONObject(SendAPI(url, method, data, token));
			System.out.println( "name:[" + retval.getString("name") +"]");
			System.out.println( "result:[" + retval.getString("result") +"]");
			
			
			/* Yearly Max Institute */
	        System.out.println("\n5. Yearly Max Institute ");
			url ="http://localhost:8080/housefund/yearlysum";
			method = "GET";			
			retval = new JSONObject(SendAPI(url, method, data, token));
			System.out.println( "name:[" + retval.getString("name") +"]");
			System.out.println( "result:[" + retval.getString("result") +"]");
			
			
			/* Yearly Max Institute */
	        System.out.println("\n6. Yearly Max Institute ");
			url ="http://localhost:8080/housefund/maxInst";
			method = "POST";
			data ="{    \"year\": \"2010\"}" ;			
			retval = new JSONObject(SendAPI(url, method, data, token));
			System.out.println( "year:[" + retval.getString("year") +"]");
			System.out.println( "bank:[" + retval.getString("bank") +"]");
			
			
			
			/* 2005~2016 외환은행 년도별 평균 최소값과 큰값 */
	        System.out.println("\n7. 2005~2016 외환은행 년도별 평균 최소값과 큰값  ");
			url ="http://localhost:8080/housefund/minmax";
			method = "GET";		
			retval = new JSONObject(SendAPI(url, method, data, token));
			System.out.println( "bank:[" + retval.getString("bank") +"]");
			System.out.println( "support_amount:[" + retval.getString("support_amount") +"]");
			
	        
			/* refresh */
	        System.out.println("\n8. ReFresh  (Reissue Token)");
			url ="http://localhost:8080/member/refresh";
			method = "GET";           							
			retval = new JSONObject(SendAPI(url, method, data, token));
			System.out.println( "Message:[" + retval.getString("Message") +"]");
			System.out.println( "Result:[" + retval.getString("Result") +"]");
			token = retval.getString("Result");
			
			
			/* Institute List after refresh */
	        System.out.println("\n9. Institute List after refresh");
			url ="http://localhost:8080/housefund/inslist";
			method = "GET";               							
			retval = new JSONObject(SendAPI(url, method, data, token));
			System.out.println( "name:[" + retval.getString("name") +"]");
			System.out.println( "result:[" + retval.getString("result") +"]");
			
			
			System.out.println("\n===== All Things are Good![Success] Test End. ======");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	private static String SendAPI(String ApiURL, String Method, String data, String token ) {
		HttpURLConnection connection = null;

		StringBuffer response=null;
		try {			
			
			URL apiURL = new URL(ApiURL);			                     
			connection = (HttpURLConnection) apiURL.openConnection();
	        connection.setRequestMethod(Method);
	        connection.setRequestProperty("Accept", "*/*");	        
	        
	        if (!token.equals("")) {
	        	String header = "Bearer " + token;
	        	connection.setRequestProperty("Authorization", header);
	        }
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.setUseCaches(false);
	        connection.setDoOutput(true);
	        connection.setDoInput(true);
	        
	        connection.connect();

	        if(Method.equals("POST")) {
		        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		        wr.write(data.getBytes("UTF-8"));
		        wr.flush();
		        wr.close();
	        }
	        
	        
	        int responseCode = connection.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();            	        
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   	
		System.out.println(response.toString());
		
		return response.toString();
	}

	/* (non-Java-doc)
	 * @see java.lang.Object#Object()
	 */
	public Main() {
		super();
	}
	
	

}