package sba.sms.services;



import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;


public class CourseService implements CourseI {

    @Override
    public void createCourse(Course course) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = s.beginTransaction();
            s.persist(course);
            tx.commit();

        }catch(HibernateException exception){
            if(tx!=null) tx.rollback();
            exception.printStackTrace();

        }finally{
            s.close();
        }
    }

    @Override
    public Course getCourseById(int courseId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            Course course = s.get(Course.class, courseId);
            if(course == null)
                throw new HibernateException("Did not find course");
            else return course;
        }catch(HibernateException exception){
            if(tx!=null) tx.rollback();
            exception.printStackTrace();
        }finally{
            s.close();
        }
        return new Course();
    }

    @Override
    public List<Course> getAllCourses() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Course> courseList = new ArrayList<>();
        try {
            courseList = s.createQuery("from Course", Course.class).list();
            s.close();
        }catch(HibernateException exception){
            exception.printStackTrace();
        }
        return courseList;


        // This is another method
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Course> courseList = s.createQuery("from Course", Course.class).list();
//        s.close();
//        return courseList;
    }
}
