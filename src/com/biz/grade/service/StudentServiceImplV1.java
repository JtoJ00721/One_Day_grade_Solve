package com.biz.grade.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.biz.grade.config.DBContract;
import com.biz.grade.config.Lines;
import com.biz.grade.domain.ScoreVO;
import com.biz.grade.domain.StudentVO;

public class StudentServiceImplV1 implements StudentService{

	private List<StudentVO> studentList;
	private Scanner scan;
	private String fileName;
	
	public StudentServiceImplV1() {
		studentList = new ArrayList<StudentVO>();
		scan = new Scanner(System.in);
		fileName = "src/com/biz/grade/exec/data/Student.txt";
	}
	
	// studentList를 외부에서 가져다 사용할 수 있도록 
	public List<StudentVO> getStudentList(){
		return studentList;
	}
	
	public StudentVO getStudent(String st_num) {
		
		// 1. studentVO를 null로 cler, null값을 studentVO에 할당
		StudentVO studentVO = null;
		
		//2. studentList를 (순서대로) 뒤지면서
		for(StudentVO stVO : studentList) {
			
			// 3. 매개변수로 받은 st_num가 학생정보를 나타내는지 검사
			// 4. 만약 있으면 학생정보를 studentVO에 복사하고
			// 5. 반복문을 종료
			if(stVO.getNum().equals(st_num)) {
				studentVO = stVO;
				break;
			}
			// 6. 만약 studentList에서 해당학번을 못찾으면
			// 반복문은 끝까지 진행할 것이다
		}
		
		// 7. 만약 중간에 if, break를 만나고 for문이 중단된 상태라면
		//		studentVO에는 stVO가 담겨 있을 것이고
		// 8. for반복문이 끝까지 진행되
		return studentVO;
	}
	
	@Override
	public void loadStudent() {
		
		FileReader fileReader = null;
		BufferedReader buffer = null;
		
		try {
			fileReader = new FileReader(this.fileName);
			buffer = new BufferedReader(fileReader);
			String reader ="";
			while(true) {
				reader = buffer.readLine();
				if(reader == null) {
					break;
				}
				String[] students = reader.split(":");
				StudentVO studentVO = new StudentVO();
				studentVO.setNum(students[DBContract.STUDENT.ST_NUM]);
				studentVO.setName(students[DBContract.STUDENT.ST_NAME]);
				studentVO.setDept(students[DBContract.STUDENT.ST_DEPT]);
				studentVO.setGrade(Integer.valueOf(students[DBContract.STUDENT.ST_GRADE]));
				studentVO.setTel(students[DBContract.STUDENT.ST_TEL]);
				studentList.add(studentVO);
				
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("학생정보 파일 열기 오류");
		} catch (IOException e) {
			System.out.println("학생정보 파일 읽기 오류");
		} 
		
		
	}

	@Override
	public boolean inputStudent() {
		
		StudentVO studentVO = new StudentVO(); 
		
		System.out.print("학번(end : 종료)>>");
		// 변수명 명명 규칙
		// Camel cass : 두 단어 이서아 사용할때 단어의 첫글자 대문자
		// Snake case : 두단어 이상 사용할때 단어 사이 _(under score)
		String st_num = scan.nextLine();
		if(st_num.equals("end")) {
			return false;
		}
		int intNum = 0;
		try {
			intNum = Integer.valueOf(st_num);
		} catch (Exception e) {
			System.out.println("학번은 숫자만 가능");
			System.out.println("입력한 문자열 : "+st_num);
			return true;
			
		}
		if(intNum < 1 || intNum > 99999) {
			System.out.println("학번은 1~ 99999까지만 가능");
			System.out.println("다시 입력해 주세요");
			return true;
		}
		
		// 00001 형식으로 만들기
		st_num = String.format("%05d", intNum);
		
		for(StudentVO sVO : studentList) {
			if(sVO.getNum().equals(st_num)) {
				System.out.println(st_num+  "학생정보가 이미 등록되어 있습니다.");
				return true;
			}
		}
		
		studentVO.setNum(st_num);
		
		System.out.print("이름(end : 종료)>>");
		String st_Name = scan.nextLine();
		if(st_Name.equals("end")) {
			return false;
		}
		studentVO.setName(st_Name);
		
		System.out.print("학과(end : 종료)>>");
		String st_Dept = scan.nextLine();
		if(st_Dept.equals("end")) {
			return false;
		}
		studentVO.setDept(st_Dept);
		
		System.out.print("학년(end : 종료)>>");
		String st_grade = scan.nextLine();
		if(st_grade.equals("end")) {
			return false;
		}
		int intGrade = 0;
		try {
			intGrade = Integer.valueOf(st_grade);
		} catch (Exception e) {
			System.out.println("학년은 숫자만 가능");
			System.out.println("입력한 문자열 : "+st_grade);
			return true;
			
		}
		if(intGrade < 1 || intGrade > 99999) {
			System.out.println("학년은 1~ 99999까지만 가능");
			System.out.println("다시 입력해 주세요");
			return true;
		}
		studentVO.setGrade(intGrade);
		
		System.out.print("전화번호 : 010-111-1111 형식(end : 종료)>>");
		String st_tel = scan.nextLine();
		if(st_tel.equals("end")) {
			return false;
		}
		studentVO.setTel(st_tel);
		
		// List에 추가하기
		studentList.add(studentVO);
		
		// 파일에 저장하기
		this.saveStudent();
		
		
		
		
		return true;
	}

	@Override
	public void saveStudent() {
		
		FileWriter fileWriter = null;
		PrintWriter pWriter = null;
		
		try {
			
			fileWriter = new FileWriter(this.fileName);
			pWriter = new PrintWriter(fileWriter);
			
			// 내부의 Writer buffer에 값을 기록
			for(StudentVO sVO : studentList) {
				pWriter.printf("%s:",sVO.getNum());
				pWriter.printf("%s:",sVO.getName());
				pWriter.printf("%s:",sVO.getDept());
				pWriter.printf("%s:",sVO.getGrade());
				pWriter.printf("%s:",sVO.getNum() +"" );
			}
			//Writer buffer 에 기록된 값을 파일에 저장
			pWriter.flush();
			pWriter.close();
			
			
		} catch (IOException e) {
			// e.printStackTrace();
		}
		
		
	}

	@Override
	public void studentList() {
		
		System.out.println(Lines.dLine);
		System.out.println("학생 명부  리스트");
		System.out.println(Lines.dLine);
		System.out.println("학번\t이름\t학과\t학년\t전화번호");
		System.out.println(Lines.sLine);
		for(StudentVO sVO : studentList) {
			System.out.printf("%s\t|",sVO.getNum());
			System.out.printf("%s\t|",sVO.getName());
			System.out.printf("%s\t|",sVO.getDept());
			System.out.printf("%s\t|",sVO.getGrade());
			System.out.printf("%s\t|\n",sVO.getTel());
		}
		System.out.println(Lines.dLine);
		
		
	}
	
}
