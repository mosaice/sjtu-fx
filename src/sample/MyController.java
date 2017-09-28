package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

class StudentThread extends Thread {
    private TextField[] coursesFiled;
    private String student;
    private String cookieString;
    private String termString;

    StudentThread(String student, TextField[] coursesFiled, String cookieString, String termString) {
        this.student = student;
        this.coursesFiled = coursesFiled;
        this.cookieString = cookieString;
        this.termString = termString;
    }

    public void run() {
        System.out.println("Running " +  student + " Thread");
        for (TextField field : coursesFiled) {
            if (field.getText().isEmpty() || !field.getText().matches("[0-9]*")) {
                System.out.println("null");
                break;
            }
            new Thread(() -> {
                HttpRequest request = new HttpRequest(student, cookieString, termString);
                request.requestCourse(field.getText());
            }).start();
        }
    }
}

public class MyController {
    public Button button;
    public TextField course1;
    public TextField course2;
    public TextField course3;
    public TextField course4;
    public TextField course5;
    public TextField student1;
    public TextField student2;
    public TextField student3;
    public TextField student4;
    public TextField student5;
    public TextField termFiled;
    public TextField cookieFiled;

    public void start(ActionEvent actionEvent) {
        TextField[] coursesFiled = {course1, course2, course3, course4, course5 };
        TextField[] studentsFiled = {student1, student2, student3, student4, student5 };
        String cookieString = cookieFiled.getText();
        String termString = termFiled.getText();

        for (TextField studentFiled: studentsFiled) {
            String student = studentFiled.getText();
            if (student.isEmpty() || !student.matches("[0-9]*")) {
                System.out.println("student null");
                break;
            }

            StudentThread s = new StudentThread(student, coursesFiled, cookieString, termString);

            Thread t = new Thread(s);
            t.start();
        }


    }
}


