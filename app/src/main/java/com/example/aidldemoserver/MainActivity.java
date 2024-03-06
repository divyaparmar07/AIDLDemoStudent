package com.example.aidldemoserver;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aidldemostudent.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    IStudentInterface anInterface;
    Context context;
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            anInterface = IStudentInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            anInterface = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent("com.intent.action.AIDLStudentService");
        intent.setPackage("com.example.aidldemoserver");
        bindService(intent, connection, BIND_AUTO_CREATE);

        TextView textView = findViewById(R.id.textViewAnswer);

        Button createStudent = findViewById(R.id.buttonCreateStudent);
        createStudent.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                try {
                    if (anInterface != null) {
                        Student interfaceStudent = anInterface.createStudent();
                        List<Marks> marksList = interfaceStudent.getMarks();
                        StringBuilder marksStringBuilder = new StringBuilder();

                        for (Marks marks : marksList) {
                            marksStringBuilder.append(marks.getSubjectName()).append(": ").append(marks.getMark()).append(" ");
                        }

                        String allMarks = marksStringBuilder.toString();

                        textView.setText("First Name : " + interfaceStudent.getFname() + ", Last Name : " + interfaceStudent.getLname() + ", RollNo : " + interfaceStudent.getRollno() + ", Address : " + interfaceStudent.getAddress() + ", Marks : " + allMarks);
                    } else {
                        Log.d("TAG", "anInterface null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Button getStudentDetails = findViewById(R.id.buttonGetStudentDetails);
        getStudentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ans = null;
                try {
                    if (anInterface != null) {
                        List<Marks> marksList = new ArrayList<>();
                        marksList.add(new Marks("Maths", 95));
                        marksList.add(new Marks("Science", 85));
                        marksList.add(new Marks("English", 90));
//                        Log.d("Data", marksList.toString());
                        ans = anInterface.getStudentDetails(new Student("Div", "Parmar", 123, 123456789, "Ahmedabad,Gujarat", marksList));
                    } else {
                        Log.d("TAG", "anInterface null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    throw new RuntimeException(e);
                }
                textView.setText(ans);
            }
        });

        Button getResult = findViewById(R.id.buttonGetResult);
        getResult.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                List<Marks> marksList = new ArrayList<>();
                marksList.add(new Marks("Maths", 25));
                marksList.add(new Marks("Science", 30));
                marksList.add(new Marks("English", 25));
                try {
                    if (anInterface != null) {
                        Result result = anInterface.getResult(new Student("Div", "Parmar", 123, 123456789, "Ahmedabad,Gujarat", marksList));
                        textView.setText("FirstName: " + result.getFname() + ", LastName: " + result.getLname() + ", RollNo: " + result.getRollno() + ", Percentage: " + result.getPercentage() + ", Result: " + result.isPass());
                    } else {
                        Log.d("TAG", "anInterface null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        List<Marks> marksList = new ArrayList<>();
        marksList.add(new Marks("Maths", 90));
        marksList.add(new Marks("Science", 85));
        marksList.add(new Marks("English", 95));

        Button createStudentFromBundle = findViewById(R.id.buttonCreateStudentFromBundle);
        createStudentFromBundle.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                List<Marks> marksList = new ArrayList<>();
                marksList.add(new Marks("Maths", 90));
                marksList.add(new Marks("Science", 85));
                marksList.add(new Marks("English", 95));

                Student student = new Student("Ridhi", "Patel", 123124124, 1231231231, "Rajkot", marksList);

                Bundle studentData = new Bundle();
//                ClassLoader classLoader = context.getClassLoader();
//                studentData.setClassLoader(classLoader);
//                studentData.putString("firstName", "Riddhi");
//                studentData.putString("lastName", "Parmar");
//                studentData.putInt("rollNo", 1);
//                studentData.putInt("phoneNo", 123454321);
//                studentData.putString("address", "Rajkot");
//                studentData.putParcelableArrayList("marksResult", marksList);
//                studentData.putParcelableArrayList("marksResult", marksList);
                studentData.setClassLoader(Student.class.getClassLoader());
                studentData.putParcelable("Student", student);

                try {
                    Log.d("TAG", "Go to try block");
                    if (anInterface != null) {
                        Log.d("TAG", "Go to if block");
                        Student student2 = anInterface.createStudentFromBundle(studentData);

//                        Log.d("resultMarks",student2.toString());
//                        List<Marks> receivedMarksList = student.getMarks();
//                        StringBuilder marksStringBuilder = new StringBuilder();
//                        for (Marks marks : receivedMarksList) {
//                            marksStringBuilder.append(marks.subjectName).append(": ").append(marks.mark).append("\n");
//                        }
                        List<Marks> marksList1 = student2.getMarks();
                        StringBuilder marksStringBuilder = new StringBuilder();
                        for (Marks marks : marksList1) {
                            marksStringBuilder.append(marks.getSubjectName()).append(": ").append(marks.getMark()).append(" ");
                        }

                        String allMarks = marksStringBuilder.toString();

                        textView.setText("First Name : " + student2.getFname() + ", Last Name : " + student2.getLname() + ", RollNo : " + student2.getRollno() + ", Address : " + student2.getAddress() + ", Marks : " + allMarks);
//                        textView.setText(student2.toString());
                    } else {
                        Log.d("TAG", "Go to else block");
                        Log.d("TAG", "anInterface null");
                        Toast.makeText(getApplicationContext(), "Interface Null", Toast.LENGTH_SHORT).show();
                    }
                } catch (RemoteException e) {
                    Log.d("TAG", "Go to catch block");
                    Toast.makeText(getApplicationContext(), "Crash", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }

//                Bundle studentData = new Bundle();
//                studentData.putString("firstName", "Riddhi");
//                studentData.putString("lastName", "Parmar");
//                studentData.putInt("rollNo", 1);
//                studentData.putInt("phoneNo", 123454321);
//                studentData.putString("address", "Rajkot");
//                List<Marks> m = new ArrayList<>();
//                m.add(new Marks("Maths", 90));
//                m.add(new Marks("Science", 70));
//                m.add(new Marks("English", 60));
//
//                studentData.putParcelableArrayList("marksResult", new ArrayList<>(m));
//                Log.d("studentData", studentData.toString());
//                try {
//                    if (anInterface != null) {
//                        Student retrieve = anInterface.createStudentFromBundle(studentData);
//                        textView.setText("First Name : " + retrieve.getFname() + ", Last Name : " + retrieve.getLname() + ", RollNo : " + retrieve.getRollno() + ",PhoneNo :  " + retrieve.getPhoneno() + ", Address : " + retrieve.getAddress() + ", Marks : " + retrieve.getMarks());
//
//                    } else {
//                        Log.d("TAG", "anInterface null");
//                    }
//                } catch (RemoteException e) {
//                    throw new RuntimeException(e);
//                }
            }

        });

        Button sendStudents = findViewById(R.id.buttonSendStudents);
        sendStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Student> students = new ArrayList<>();
                Student student = new Student("Divya", "Parmar", 1234, 1234567890, "Rajkot,Gujarat", marksList);
                Student student2 = new Student("Div", "Parmar", 123, 123456789, "Ahmedabad,Gujarat", marksList);
                Student student3 = new Student("Riddhi", "Parmar", 12, 123454321, "Rajkot,Gujarat", marksList);

                students.add(student);
                students.add(student2);
                students.add(student3);

                try {
                    String ans = anInterface.sendStudents(students);
                    textView.setText(ans);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Button sendStudentEnum = findViewById(R.id.buttonSendStudentEnum);
        sendStudentEnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int ans = anInterface.sendStudentEnum(EnumStudent.PARTHIV);
                    Log.d("ans", "" + ans);
                    textView.setText(String.valueOf(ans));
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}