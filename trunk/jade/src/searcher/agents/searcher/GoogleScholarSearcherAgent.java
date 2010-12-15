package searcher.agents.searcher;

import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import searcher.Article;

public class GoogleScholarSearcherAgent extends SearcherAgent {

	private static final String MAIN_HEADER_PART1 = "GET /scholar?start=";
	private static final String MAIN_HEADER_PART2 = "&q=";
	private static final String MAIN_HEADER_PART3 = " HTTP/1.1\n"
			+ "Host: scholar.google.com\n"
			+ "User-Agent: GoogleScholarSearcherAgent\n" + "\n";
	private static final String HOST = "scholar.google.com";
	private static final int PORT = 80;
	private static final String TEMP_REPLY_FILE = "tempRepltFileForGScholar";
	private static final String SUBSTRING_FOR_ARTICLE = "<div class=gs_r><div class=gs_rt><h3>";
	private static final String SUBSTRING_FOR_AUTHORS_AND_SUMMARY = "<span class=gs_a>";
	/**
	 * 10 - is standart for GScholar. Don't change!!!
	 */
	private static final int AMOUNT_OF_RESULTS_ON_EACH_REQUEST = 10;
	private static final int SIZE_OF_BUF_ON_WRITE = 64 * 1024;
	private static final int SIZE_OF_BUF_ON_READ = 64 * 1024;
	private static int testIndex = 1;
	private int curIndexArticle = 0;


	private void processDatasFromFileAndSendMSGs() {
		String fileContent = getfileContent();
		// System.out.println(fileContent);
		int startIndexArticle = fileContent.indexOf(SUBSTRING_FOR_ARTICLE, 0)
				+ SUBSTRING_FOR_ARTICLE.length();
		int curIndex = -1;
		while (startIndexArticle != -1 + SUBSTRING_FOR_ARTICLE.length()) {
			int startIndexOfHREF = fileContent.indexOf("href=\"", startIndexArticle)+6;
			curIndex = fileContent.indexOf('"', startIndexOfHREF);
			String url = fileContent.substring(startIndexOfHREF, curIndex);
			
			int startIndexOfTitle = fileContent.indexOf(">", curIndex);
			curIndex = fileContent.indexOf("</a>", startIndexOfTitle);
			String title = fileContent.substring(startIndexOfTitle+1, curIndex);
			
			int saveCurIndex = curIndex;
			
			int startIndexOfAuthors = fileContent.indexOf(SUBSTRING_FOR_AUTHORS_AND_SUMMARY, curIndex);
			curIndex = fileContent.indexOf("</span>", startIndexOfAuthors);
			String authors = fileContent.substring(startIndexOfAuthors+SUBSTRING_FOR_AUTHORS_AND_SUMMARY.length(), curIndex);
			
			int startIndexOfSummary = curIndex+7;
			curIndex =  fileContent.indexOf("<span", startIndexOfSummary);
			String summary = fileContent.substring(startIndexOfSummary, curIndex);
			
			if(fileContent.substring(saveCurIndex, curIndex).contains(SUBSTRING_FOR_ARTICLE)){
				curIndex = saveCurIndex;
				
				authors = "no author: Exception: this article possibly has error";
				summary = "no summary: Exception: this article possibly has error";
			}else{
				/*
				System.out.println(testIndex + ") "  + " title = " + title);
				System.out.println("  url = "+ url );
				System.out.println("  authors = " + authors);
				System.out.println("  summury = " + summary);
				*/
				//System.out.println("_____________________" + curIndexArticle + "_________________________");
				//System.out.println("______________________________________" + summary);
				Article	article = new Article(url,"noPDF",title,summary,authors,"unknown","unknown");
				article.addSearcherSenderAndRank(this.getName(), getCurRankArticle(curIndexArticle));
				this.sendArticle(article);
			}
			
			startIndexArticle = curIndex;
			startIndexArticle = fileContent.indexOf(SUBSTRING_FOR_ARTICLE,
					startIndexArticle) + SUBSTRING_FOR_ARTICLE.length();
			testIndex++;
			curIndexArticle++;
		}

	}

	private String getfileContent() {
		int r = 0;
		byte[] buf = new byte[SIZE_OF_BUF_ON_READ];
		try {
			// читаем файл с запросом в переменную header
			FileInputStream fis;
			fis = new FileInputStream(TEMP_REPLY_FILE);
			r = fis.read(buf);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(buf, 0, r);
	}

	private void doRequestAndWriteToFile(String request, int numberStart) {
		try {
			// открываем сокет до сервера
			Socket s = new Socket(HOST, PORT);
			// пишем туда HTTP request
			s.getOutputStream().write(
					getHeader(request, numberStart).getBytes());
			// получаем поток данных от сервера
			InputStream is = s.getInputStream();
			// Открываем для записи файл, куда будет слит лог
			FileOutputStream fos = new FileOutputStream(TEMP_REPLY_FILE);
			// читаем ответ сервера, одновременно сливая его в открытый файл
			byte buf[] = new byte[SIZE_OF_BUF_ON_WRITE];
			int r = 5;
			while (r > 0 && !endDatas(buf, r)) {
				r = is.read(buf);
				if (r > 0) {
					fos.write(buf, 0, r);
				}
			}
			// закрываем файл
			fos.close();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		} // вывод исключений
	}

	/**
	 * Этот метод должен ускорять процесс считывания потока. Т.к. когда считан
	 * весь поток, то при очередном считывании процесс в строчке r =
	 * is.read(buf); почему-то подвисает. Предпологается, что файл закончен,
	 * если прочли строчку "<"+"/html>"
	 * 
	 * @param buf
	 * @param r
	 * @return
	 */
	private static boolean endDatas(byte[] buf, int r) {
		/*
		 * if (buf[r - 3] == 10 && buf[r - 2] == 13 && buf[r - 1] == 10) {
		 * return true; }
		 */
		String bufSt = new String(buf, 0, r);
		if (bufSt.indexOf("</html>") != -1) {
			return true;
		}
		return false;
	}

	private String getHeader(String request, int numberStart) {
		String header = MAIN_HEADER_PART1 + numberStart + MAIN_HEADER_PART2;
		for (StringTokenizer st = new StringTokenizer(request); st
				.hasMoreTokens();) {
			header += st.nextToken();
			if (st.hasMoreTokens()) {
				header += "+";
			}
		}
		header += MAIN_HEADER_PART3;
		return header;
	}


	// ----------------------------------
	@Override
	public void searchAndSendResults(ACLMessage msg) {
		String request = msg.getContent();
		curIndexArticle = 0;
		for (int numberStart = 0; numberStart < MAX_AMOUNT_OF_RESULTS_ON_ONE_REQUEST; numberStart += AMOUNT_OF_RESULTS_ON_EACH_REQUEST) {
			assert curIndexArticle == numberStart;
			doRequestAndWriteToFile(request, numberStart);
			processDatasFromFileAndSendMSGs();
		}
	}

	@Override
	public void setSourceValue() {
		sourseValue = "GoogleScholar";
	}

}
