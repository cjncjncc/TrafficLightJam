import java.util.HashSet;
import java.util.Set;


public class LongestPalindromicSubstring {
	public static String longestPalindrome(String s) {
		Set<Pair> set=new HashSet<Pair>();
		int max=0;
		Pair Finalpair=null;
		String ret = null;
		for(int i=0;i<s.length();i++){
			if(i>0&&s.charAt(i)==s.charAt(i-1)){
				Pair add=new Pair(i-1,i);
				set.add(add);
			}
			if(i>1&&s.charAt(i)==s.charAt(i-2)){
				Pair add=new Pair(i-2,i);
				set.add(add);
			}
			Set<Pair> CanditSet= new HashSet<Pair>();
				for(Pair candi:set){
					if(max<candi.end-candi.Begin+1){
						max=candi.end-candi.Begin+1;
						Finalpair=candi;
					}
					if(candi.Begin>0&&candi.end<s.length()-1&&s.charAt(candi.Begin-1)==s.charAt(candi.end+1)){
						Pair add=new Pair(candi.Begin-1,candi.end+1);
						CanditSet.add(add);
					}
				}
			set=CanditSet;
			
		}
	
		if(Finalpair!=null){
			ret=s.substring(Finalpair.Begin, Finalpair.end+1);
		}
		if(s.length()==1){
			return s;
		}else{
			return ret;
		}
		
    }
}
