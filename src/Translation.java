import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Translation {
	public static void main(String[] args) {
//		ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
		// 繁体转换简体
//		String traditionalSrc = "核心提示：澳大利亞FAXTS新聞3月5日刊登評論認為，美國在全球一系列被解釋成用來防禦來自伊朗和朝鮮導彈襲擊的彈道導彈防禦系統的部署行為，以及最近將先進具有反導能力愛國者導彈出售給台灣的動作，其根本目的是針對兩個主要的核大國--中國和俄羅斯。";
//		String simplified = converter.convert(traditionalSrc);
//		System.out.println(simplified);
		// 简体转换繁体
//		String traditional = ZHConverter.convert(simplified, ZHConverter.TRADITIONAL);
//		System.out.println(traditional);
		
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.TRADITIONAL);
		try {
			FileInputStream fis = new FileInputStream(new File("strings.xml"));
			PrintStream ps = new PrintStream(new File("strings2.xml"));
			BufferedReader br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
			String str = br.readLine();
			while (str != null) {
				if(str.contains("\">")){
					String str2 = str.substring(str.indexOf("\">")+2,str.indexOf("</string>"));
					String ss = converter.convert(str2);
					String text  = str.replace(str2, ss);
					ps.println(text);
				}else{
					ps.println(str);
				}
				str = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("完成了!");
	}

}
