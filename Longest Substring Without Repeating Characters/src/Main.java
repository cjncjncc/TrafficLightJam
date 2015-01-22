import java.util.HashSet;
import java.util.*;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a="wlrbbmqbhcdarzowkkyhiddqscdxrjmowfrxsjybldbefsarcbynecdyggxxpklorellnmpapqfwkhopkmco";
		System.out.println(lengthOfLongestSubstring(a));
	}
	   public static int lengthOfLongestSubstring(String s) {
	        Map<Character,Integer> CharacterSize=new HashMap<Character,Integer>();
	        int max=0;
	        int begin=0;
	        for(int i=0;i<s.length();i++){

	        	if(!CharacterSize.containsKey(s.charAt(i))){
	        		CharacterSize.put(s.charAt(i), i);

	        		
	        	}else{
	        		int CharAt=CharacterSize.get(s.charAt(i));
	        		for(int j=begin;j<=CharAt;j++){
	        			CharacterSize.remove(s.charAt(j));
	        		}
	        		CharacterSize.put(s.charAt(i), i);
	        		begin=CharAt+1;
	        	}
	        	if(max<CharacterSize.size()){
        			max=CharacterSize.size();
        		}
	        }
	        return max;
	    }
}
