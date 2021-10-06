package com.itradix.ehealth.service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdGenService {
	
	@Autowired
	S3XmlService s3XmlService;

	public IdGenService() {

		
	}
	
	public static String padLeftZeros(String inputString, int length) {
	    if (inputString.length() >= length) {
	        return inputString;
	    }
	    StringBuilder sb = new StringBuilder();
	    while (sb.length() < length - inputString.length()) {
	        sb.append('0');
	    }
	    sb.append(inputString);
	 
	    return sb.toString();
	}

	public static String genId(int day)
    {
		
	  long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
      long old = LocalDateTime.of(2012, Month.MARCH, day, 19, 30, 40).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
      long result = now - old;
      String str = Long.toString(result);
      String vstupneCislo = str.substring(str.length() - 10, 10);
      vstupneCislo = padLeftZeros(vstupneCislo, 10);
      int num = LuhnCislo(vstupneCislo);
      return vstupneCislo + vstupneCislo + Integer.toString(num);
    }

	public static int LuhnCislo(String vstupneCislo) throws NumberFormatException {
		int num1 = -1;
		
		if ((vstupneCislo == null || vstupneCislo.equals(""))) 
			return num1;
		int num2 = 0;
		for (int startIndex = 0; startIndex < vstupneCislo.length(); ++startIndex) {
			int result;
			try {
				result = Integer.parseInt(vstupneCislo.charAt(startIndex)+"");
			} catch (NumberFormatException nfe) {
				return num1;
			}
			num2 += result;
		}
		int[] numArray = { 0, 1, 2, 3, 4, -4, -3, -2, -1, 0 };
		for (int startIndex = vstupneCislo.length() - 1; startIndex >= 0; startIndex -= 2) {
			int index = Integer.parseInt(vstupneCislo.charAt(startIndex)+"");
			int num3 = numArray[index];
			num2 += num3;
		}
		int num4 = 10 - num2 % 10;
		if (num4 == 10)
			num4 = 0;
		return num4;
	}
	
	@Test
	public void testGenerator() {
		new IdGenService();
		
	} 

}
