package InfoSearchQuiz_jhlee;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

public class Test04 {
	
	public static double[] mapToList(HashMap<String, Double> m) {
		double[] output = new double[m.size()];
		int i=0;
		Iterator<String> keys = m.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			output[i] = m.get(key);
			i++;
		}
		return output;
	}
	
	public static double cosinSimilarity(double[] A,double[] B) {
	      
	      if(A==null||B==null||A.length==0||B.length==0||A.length != B.length) {
	         return 0;
	      }
	      
	      double sumProduct = 0;
	      double sumAsq = 0;
	      double sumBsq = 0;
	      for(int i = 0 ; i < A.length;i++) {
	         
	         sumProduct += A[i]*B[i];
	         sumAsq += A[i]*A[i];
	         sumBsq += B[i]*B[i];
	      }
	      
	      if(sumAsq == 0 && sumBsq ==0) {
	         return 0.0;
	      }
	      double db = sumProduct / (Math.sqrt(sumAsq)*Math.sqrt(sumBsq));
	      
	      return db;
	   }

	public static ArrayList<String> sortHSbyValue_double(HashMap<String, Double> target) {
	      
	      ArrayList<Map.Entry<String, Double>> k = new ArrayList<>(target.entrySet());
	      Collections.sort(k, new Comparator<Map.Entry<String, Double>>() {
	         @Override
	         public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
	            int comparison = (o2.getValue() > o1.getValue() ? 1 : -1);
	               if(o2.getValue()==(o1.getValue())) comparison = o1.getKey().compareTo(o2.getKey());
	               return   comparison;
	         }
	      });
	      
	      ArrayList<String> top5KeyList = new ArrayList<>();
	      for(int i=0; i<5; i++) {
	         top5KeyList.add(k.get(i).getKey());
	      }
	      
	      return top5KeyList;
	   }
	
	public static void main(String[] args, String str) {
		// TODO Auto-generated method stub
		HashMap<String, HashMap<String, Double>> file_alph_tf = new HashMap<String, HashMap<String, Double>>();
		HashMap<String, Double> file_alph_idf = new HashMap<String, Double>();
		HashMap<String, Double> tmp_idf = new HashMap<String, Double>();
		HashMap<String, HashMap<String, Double>> result_map = new HashMap<String, HashMap<String, Double>>();
		// 이후에 cosine similarity를 계산할때 사용할 top5해쉬맵
		HashMap<String, HashMap<String, Double>> top5 = new HashMap<String, HashMap<String, Double>>();
		
		Test01 t1 = new Test01();
		Test02 t2 = new Test02();
		Test03 t3 = new Test03();
		
		// 입력
		
		File[] fileList = t1.get_file();
		
		for (File file : fileList) {
			
			HashMap <String, Double> tmp_map = new HashMap <String, Double>();
			
			// count alphabet _ each files
			try {
				FileReader file_reader = new FileReader(file);
				int cur = 0;
				while ((cur = file_reader.read()) != -1) {
					if (cur != 32) {
						if (!tmp_map.containsKey(String.valueOf((char)cur))) {
							tmp_map.put(String.valueOf((char)cur), (double) 1);
						} else {
							double temp_num = tmp_map.get(String.valueOf((char)cur));
							tmp_map.put(String.valueOf((char)cur),temp_num+1);
						}
					}
				}
			} catch(FileNotFoundException e) {
				e.getStackTrace();
			} catch(IOException e) {
				e.getStackTrace();
			}
			
			
			
			// calc_TF
			ArrayList<String> key_lst = new ArrayList<String>(tmp_map.keySet());
			double sum_value = 0;
			// TF의 분모 문서내에 오는 모든수 더하기
			for (String k : key_lst) {
				sum_value += tmp_map.get(k);	
			}
			// TF의 분자 : 해당 파일에 있는 알파벳의 갯수 (tmp_map에 저장되어있음) / tf분모
			for (String tf_k : key_lst) {
				double tf = tmp_map.get(tf_k) / sum_value;
				tmp_map.put(tf_k, tf);
			}
			sum_value = 0;
			file_alph_tf.put(file.getName(), tmp_map);
			
			// calc_IDF_Bottom : counting IDF_Bottom
			tmp_idf = file_alph_tf.get(file.getName());
			
			for (String idf_k : key_lst) {
				if (tmp_idf.get(idf_k) != 0) {
					if (!file_alph_idf.containsKey(idf_k)) {
						file_alph_idf.put(idf_k, (double) 1);
					}else {
						double tmp_num = file_alph_idf.get(idf_k);
						file_alph_idf.put(idf_k, tmp_num+1);
					}
				}
			}
			
		} // file의 이름으로 반복
		
		
		
		// calc_IDF
		for (File file : fileList) {
			HashMap<String, Double> aa = new HashMap<String, Double>();
			ArrayList<String> key_lst = new ArrayList<String>(file_alph_idf.keySet());
			double temp_num = 0;
			for (String key : key_lst) {
				
				double tf;
				temp_num = 0;
				try {
					tf = file_alph_tf.get(file.getName()).get(key);
					if (file_alph_idf.get(key) != 0) {
						double tt = file_alph_idf.get(key);
						temp_num = 7/tt;
					} else {
						temp_num = 0;
					}
				} catch(NullPointerException e) {
					tf = 0;
				}
				double idf = Math.log(1+ temp_num);
				double tfidf = tf*idf;
				aa.put(key, tfidf);
			}
			result_map.put(file.getName(), aa);
		}	
	
		
		
		
		for(File f : fileList) {
			
	//---------------------------------------상위 5개-------------------------------------------			
			
//			
			String f_n = f.getName(); 
//			System.out.println(f_n);
			// 0. 정렬 전 파일 출력
//			ArrayList <String> key_lst = new ArrayList<String>(result_map.get(f_n).keySet());
//			for (String kk : key_lst) {
//				System.out.println(kk + " : " + result_map.get(f_n).get(kk));
//			}
			
			ArrayList<String> Top5List = sortHSbyValue_double(result_map.get(f_n));
			HashMap<String, Double> map = new HashMap<String, Double>();
			for (String s : Top5List) {
				map.put(s, result_map.get(f_n).get(s));
			}

			top5.put(f_n, map);
			
		}
		
		// cosine_similarity
		// 구하기 전에 벡터의 차원을 맞춰준다.
		// input받는 파일을 제외한 나머지 파일들과의 유사도를 구한다.
		String input = str;
		HashMap<String, Double> csMap = new HashMap<String, Double>();
		
		for (File f : fileList) {
			HashMap<String, Double> a = (HashMap<String, Double>) top5.get(input).clone();
			ArrayList<String> a_key_lst = new ArrayList<String>(a.keySet());
			ArrayList<String> total_key = new ArrayList<String>();
			
			if (input.equals(f.getName())) {
				continue;
			}
			HashMap<String, Double> b = (HashMap<String, Double>) top5.get(f.getName()).clone();
			ArrayList<String> b_key_lst = new ArrayList<String>(b.keySet());
			
			for (String a_k : a_key_lst) {
				total_key.add(a_k);
			}
			for (String b_k : b_key_lst) {
				total_key.add(b_k);
			}
			
			for (String t_k : total_key) {
				if(!a.containsKey(t_k)) {
					a.put(t_k, (double)0);
				}
				if(!b.containsKey(t_k)) {
					b.put(t_k, (double)0);
				}
			}
			
			double[] a_lst = t3.mapToList(a);
			double[] b_lst = t3.mapToList(b);
			double cs = cosinSimilarity(a_lst, b_lst);
			csMap.put(f.getName(), cs);
			
			a_key_lst.clear();
			total_key.clear();
			
		}

		t2.sort_print(csMap, input);
		
		System.out.println();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, HashMap<String, Double>> file_alph_tf = new HashMap<String, HashMap<String, Double>>();
		HashMap<String, Double> file_alph_idf = new HashMap<String, Double>();
		HashMap<String, Double> tmp_idf = new HashMap<String, Double>();
		HashMap<String, HashMap<String, Double>> result_map = new HashMap<String, HashMap<String, Double>>();
		// 이후에 cosine similarity를 계산할때 사용할 top5해쉬맵
		HashMap<String, HashMap<String, Double>> top5 = new HashMap<String, HashMap<String, Double>>();
		
		Test01 t1 = new Test01();
		Test02 t2 = new Test02();
		Test03 t3 = new Test03();
		
		// 각 문서의 특성을 추출 : Test04 - map		
		
		File[] fileList = t1.get_file();
		
		for (File file : fileList) {
			
			HashMap <String, Double> tmp_map = new HashMap <String, Double>();
			
			// count alphabet _ each files
			try {
				FileReader file_reader = new FileReader(file);
				int cur = 0;
				while ((cur = file_reader.read()) != -1) {
					if (cur != 32) {
						if (!tmp_map.containsKey(String.valueOf((char)cur))) {
							tmp_map.put(String.valueOf((char)cur), (double) 1);
						} else {
							double temp_num = tmp_map.get(String.valueOf((char)cur));
							tmp_map.put(String.valueOf((char)cur),temp_num+1);
						}
					}
				}
			} catch(FileNotFoundException e) {
				e.getStackTrace();
			} catch(IOException e) {
				e.getStackTrace();
			}
			
			
			// calc_TF
			ArrayList<String> key_lst = new ArrayList<String>(tmp_map.keySet());
			double sum_value = 0;
			// TF의 분모 문서내에 오는 모든수 더하기
			for (String k : key_lst) {
				sum_value += tmp_map.get(k);	
			}
			// TF의 분자 : 해당 파일에 있는 알파벳의 갯수 (tmp_map에 저장되어있음) / tf분모
			for (String tf_k : key_lst) {
				double tf = tmp_map.get(tf_k) / sum_value;
				tmp_map.put(tf_k, tf);
			}
			sum_value = 0;
			file_alph_tf.put(file.getName(), tmp_map);
			
			
			
			// calc_IDF_Bottom : counting IDF_Bottom
			tmp_idf = file_alph_tf.get(file.getName());
			
			for (String idf_k : key_lst) {
				if (tmp_idf.get(idf_k) != 0) {
					if (!file_alph_idf.containsKey(idf_k)) {
						file_alph_idf.put(idf_k, (double) 1);
					}else {
						double tmp_num = file_alph_idf.get(idf_k);
						file_alph_idf.put(idf_k, tmp_num+1);
					}
				}
			}	
		} // file의 이름으로 반복
		
		
		// calc_IDF
		for (File file : fileList) {
			HashMap<String, Double> aa = new HashMap<String, Double>();
			ArrayList<String> key_lst = new ArrayList<String>(file_alph_idf.keySet());
			double temp_num = 0;
			for (String key : key_lst) {
				
				double tf;
				temp_num = 0;
				try {
					tf = file_alph_tf.get(file.getName()).get(key);
					if (file_alph_idf.get(key) != 0) {
						double tt = file_alph_idf.get(key);
						temp_num = 7/tt;
					} else {
						temp_num = 0;
					}
				} catch(NullPointerException e) {
					tf = 0;
				}
				double idf = Math.log(1+ temp_num);
				double tfidf = tf*idf;
				aa.put(key, tfidf);
			}
//			System.out.println();
			result_map.put(file.getName(), aa);
		}	
	
		
		
		
		for(File f : fileList) {
			
	//---------------------------------------상위 5개-------------------------------------------			
			
//			System.out.println(f.getName());
			String f_n = f.getName(); 
			
			// 0. 정렬 전 파일 출력
//			ArrayList <String> key_lst = new ArrayList<String>(result_map.get(f_n).keySet());
//			for (String kk : key_lst) {
//				System.out.println(kk + " : " + result_map.get(f_n).get(kk));
//			}
			
			ArrayList<String> Top5List = sortHSbyValue_double(result_map.get(f_n));
//			System.out.println(Top5List);
			HashMap<String, Double> map = new HashMap<String, Double>();
			for (String s : Top5List) {
				map.put(s, result_map.get(f_n).get(s));
			}

			top5.put(f_n, map);	
		}
		
		// cosine_similarity
		// 구하기 전에 벡터의 차원을 맞춰준다.
		// input받는 파일을 제외한 나머지 파일들과의 유사도를 구한다.

		Scanner sc = new Scanner(System.in);
		System.out.print("문서 이름을 입력하세요 : ");
		String input = sc.next();
		
		HashMap<String, Double> csMap = new HashMap<String, Double>();
		
		for (File f : fileList) {
			HashMap<String, Double> a = (HashMap<String, Double>) top5.get(input).clone();
			ArrayList<String> a_key_lst = new ArrayList<String>(a.keySet());
			ArrayList<String> total_key = new ArrayList<String>();
			
			if (input.equals(f.getName())) {
				continue;
			}
			HashMap<String, Double> b = (HashMap<String, Double>) top5.get(f.getName()).clone();
			ArrayList<String> b_key_lst = new ArrayList<String>(b.keySet());
			
			for (String a_k : a_key_lst) {
				total_key.add(a_k);
			}
			for (String b_k : b_key_lst) {
				total_key.add(b_k);
			}
			
			for (String t_k : total_key) {
				if(!a.containsKey(t_k)) {
					a.put(t_k, (double)0);
				}
				if(!b.containsKey(t_k)) {
					b.put(t_k, (double)0);
				}
			}
			
			double[] a_lst = t3.mapToList(a);
			double[] b_lst = t3.mapToList(b);
			double cs = cosinSimilarity(a_lst, b_lst);
			csMap.put(f.getName(), cs);
			
			a_key_lst.clear();
			total_key.clear();
			
		}
		
		t2.sort_print(csMap, input);
		
		System.out.println();
	}
}