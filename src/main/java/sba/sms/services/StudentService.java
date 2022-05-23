package sba.sms.services;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;


public class StudentService implements StudentI {

    // create Student method
    @Override
    public void createStudent(Student student) {
        // create a section factory
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try{
            tx = s.beginTransaction();
            s.persist(student);
            tx.commit();
        }catch(HibernateException exception){
            if(tx!=null) tx.rollback();
            exception.printStackTrace();
        }finally{
            s.close();
        }
        // return student upon creation

    }

    @Override
    public List<Student> getAllStudents() {

        Session s = HibernateUtil.getSessionFactory().openSession();
            List<Student> allStudentList = new ArrayList<>();
            try {
               allStudentList = s.createQuery("from Student", Student.class).list();
               s.close();
           }catch(HibernateException exception){
               exception.printStackTrace();
           }
        return allStudentList;
    }


    @Override
    public Student getStudentByEmail(String email) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx =null;

        try{
            Student student = s.get(Student.class, email);
            if(student==null)
                throw new HibernateException("Did not find email");
            else
                return student;
        }catch(HibernateException exception){
            // rollback or cancel
            if(tx!=null) tx.rollback();
            exception.printStackTrace();
        }finally{
            s.close();
        }
        return new Student();
    }

    @Override
    public boolean validateStudent(String email, String password) {
        List <Student> listSt = getAllStudents();
        for(Student student : listSt){
            if(student.getEmail().equals(email) && student.getPassword().equals(password))
                return true;
        }
        return false;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx =null;
        try{
            tx=s.beginTransaction();
            Course c = s.get(Course.class, courseId);
            Student st = s.get(Student.class, email);
            st.addCourse(c);
            // save and update
            s.merge(st);
            tx.commit();
        }catch(HibernateException exception){
            if(tx!=null) tx.rollback();
            exception.printStackTrace();
        }finally{
            s.close();
        }
    }

    @Override
    public List<Course> getStudentCourses(String email) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Course> courseList = null;
        try{
            tx = s.beginTransaction();
            // join student and course table with student_courses table using FK
            NativeQuery q = s.createNativeQuery("select cr.id, cr.name, cr.instructor from course as cr join student_courses as sc on cr.id = sc.courses_id join student as st on st.email = sc.student_email where st.email = :email", Course.class);
            q.setParameter("email", email);
            courseList = q.getResultList();
            tx.commit();

        }catch(HibernateException exception){
            if(tx!=null) tx.rollback();
            exception.printStackTrace();
        }finally{
            s.close();
        }

        return courseList;
    }
}
