package vs_ub1_java_sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) throws IOException {
		
		String hostName = "vs.lxd-vs.uni-ulm.de";
		int port = 5678; // 3211 für Echo-Anfragen	
		String text = "Please provide a new expression!\n";
				
		byte[] sendBuffer = text.getBytes(StandardCharsets.UTF_8);
		
		// Send UDP 		
		System.out.println("Starte UDP-Socket...");

		DatagramSocket datagramSocket = new DatagramSocket();

		DatagramPacket packet = new DatagramPacket(
									sendBuffer, 
									sendBuffer.length,
									InetAddress.getByName(hostName), port
									);
		
		datagramSocket.send(packet);
		
		System.out.println("Paket gesendet.");
		
        byte[] respBuffer = new byte[1024];
        DatagramPacket response = new DatagramPacket(respBuffer, respBuffer.length);

		datagramSocket.receive(response);

        String responseString = new String(
                					response.getData(),
                					response.getOffset(),
                					response.getLength()-1,
                					StandardCharsets.UTF_8
        							);
        
		System.out.println("Empfangen: " + "["+responseString+"]");
		String testString = "6*9*5*7*4";
				
		String resultString = EvaluateTheTerm(responseString);
		System.out.println("Ergebnis: " + resultString);
		
		
		// Send result via UDP
		String resultStringToSend = responseString + "=" + resultString + "\n";
		byte[] sendResultBuffer = resultStringToSend.getBytes(StandardCharsets.UTF_8);
		


		DatagramPacket resultPacket = new DatagramPacket(
										sendResultBuffer, 
										sendResultBuffer.length,
										InetAddress.getByName(hostName), port
										);
		
		datagramSocket.send(resultPacket);
		
		// Wait for positive.
		byte[] answerBuffer = new byte[1024];
        DatagramPacket answer = new DatagramPacket(answerBuffer, answerBuffer.length);

		datagramSocket.receive(answer);

        String answerString = new String(
        							answer.getData(),
        							answer.getOffset(),
        							answer.getLength(),
                					StandardCharsets.UTF_8
        							);
        
		System.out.println("=================================");
		System.out.print("Antwort: " + answerString);
		System.out.println("=================================");
		
		
		datagramSocket.close();
	}
	
	public static String EvaluateTheTerm(String term) 
	{
		int result = 0;
		String[] chars = term.split("\\+");
		//for(int a = 0; a < chars.length; a++) System.out.println(chars[a]);
		
		ArrayList<String> summands = new ArrayList<>(Arrays.asList(chars));
		
		for(String summand : summands) {
			if(summand.length() != 1) {
				// => summands is a multiplication
				result += calculateMultiplicationString(summand);
			} else if(summand.length() == 1) {
				result += Integer.parseInt(summand);
			}
		}
				
		return Integer.toString(result);
		
	}
	
	// Calculates given string representing a multiplication of x factors.
	
	// TEST
	public static int calculateMultiplicationString(String multiplicationString) 
	{	
		int result = 1; 

		ArrayList<String> factors = new ArrayList<>(Arrays.asList(multiplicationString.split("\\*")));
		
		for(String factor : factors) {
			result = result * Integer.parseInt(factor);
		}		
		
		System.out.println("Subtermresult: " + result);
		
		return result;
	}

}
