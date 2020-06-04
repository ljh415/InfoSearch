package InfoSearchQuiz_jhlee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Test05 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Test01 t1 = new Test01();
		Test02 t2 = new Test02();
		Test04 t4 = new Test04();
		
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			String input_str;
			
			System.out.println("----------------------------------------------------------------");
			System.out.println("1. 입력된 텍스트의 TF-IDF를 텍스트 파일별로 출력 (입력 : 1 텍스트(알파벳))" );
			System.out.println("2. 입력된 텍스트의 co-occurrence를 빈도순으로 출력 (입력 : 2 텍스트(알파벳)");
			System.out.println("3. 입력된 파일과 코사인 유사도가 높은 순으로 파일 출력 (입력 : 3 파일명)");
			System.out.println("4. 입력된 경로의 파일을 추가하여 1,2,3번 입력 대기 (입력 : 4 디렉토리경로)");
			System.out.println("5. 프로그램 종료 (입력 : 5)");
			System.out.println("----------------------------------------------------------------");
			System.out.print("입력하세요 : ");
			int input_integer = sc.nextInt();
			if (input_integer == 5 ) {
				input_str = null;
			} else {
				input_str = sc.next();
			}
			
			switch (input_integer) {
			
				case 1:
					t1.main(args, input_str);
					break;
				case 2:
					t2.main(args, input_str);
					break;
				case 3:
					t4.main(args, input_str);
					break;
				case 4: 
//					System.out.print("입력하세요 : ");
					String path = input_str;
					File folder1 = new File(path);
					File folder2 = new File(".\\searchInfo_SampleFile_1");
					Test05.copy(folder1, folder2);
					System.out.println("경로가 추가되었습니다.");
					break;
				case 5:
					System.out.print("프로그램을 종료합니다.");
					return;
			}
		}
	}

	public static void copy(File sourceF, File targetF){
		File[] target_file = sourceF.listFiles();
		for (File file : target_file) {
			File temp = new File(targetF.getAbsolutePath() + File.separator + file.getName());
			if(file.isDirectory()){
				temp.mkdir();
				copy(file, temp);
			} else {
			        FileInputStream fis = null;
				FileOutputStream fos = null;
				try {
					fis = new FileInputStream(file);
					fos = new FileOutputStream(temp) ;
					byte[] b = new byte[4096];
					int cnt = 0;
					while((cnt=fis.read(b)) != -1){
						fos.write(b, 0, cnt);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					try {
						fis.close();
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
				}
			}
		}
	}
}
