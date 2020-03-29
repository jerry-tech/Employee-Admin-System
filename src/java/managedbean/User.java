package managedbean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.sql.*;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ManagedBean
@RequestScoped

public class User {

    String id, fname, lname, gender, marital, address, department;

    /*  Validating the users bio-data form using annotations in jsf */
    @Size(min = 14, max = 14, message = "Phone Number Columns Must Follow this pattern [+234(0)7000000000]")
    @Pattern(regexp = "[+]{1}[2-4]{3}[0-9]{10}")
    String phone;

    @Pattern(regexp = "[a-z A-Z _0-9]+@[a-z]+[.][a-z]+")
    String email;

    @Min(18)
    @Max(60)
    String age;

    ArrayList usersList;
    Connection con;
    private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMarital() {
        return marital;
    }

    public void setMarital(String marital) {
        this.marital = marital;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=jsfEmp;user=sa;password=haansbro17");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        return con;
    }//creating connection

    public boolean save() {
        int result = 0;

        try {
            con = getConnection();

            PreparedStatement ps = con.prepareStatement("insert into users.Employee values(?,?,?,?,?,?,?,?,?)");

            ps.setString(1, this.getFname());
            ps.setString(2, this.getLname());
            ps.setString(3, this.getAge());
            ps.setString(4, this.getGender());
            ps.setString(5, this.getDepartment());
            ps.setString(6, this.getMarital());
            ps.setString(7, this.getAddress());
            ps.setString(8, this.getPhone());
            ps.setString(9, this.getEmail());

            result = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }
        if (result == 1) {
            return true;
        } else {
            return false;
        }

    }

    public String submit() {
        if (this.save()) {
            return "index.xhtml?faces-redirect=true";
        } else {
            return "index.xhtml";
        }
    }

//    =========================================================================
//    querying all records from database
    public ArrayList usersList() {
        try {
            usersList = new ArrayList();

            con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from users.Employee");
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getString("EmpId"));
                user.setFname(rs.getString("firstname"));
                user.setLname(rs.getString("lastname"));
                user.setAge(rs.getString("age"));
                user.setGender(rs.getString("Gender"));
                user.setDepartment(rs.getString("Department"));
                user.setMarital(rs.getString("Marital"));
                user.setAddress(rs.getString("Address"));
                user.setPhone(rs.getString("Phone"));
                user.setEmail(rs.getString("Email"));

                usersList.add(user);
            }
            con.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
        return usersList;
    }

    public void delete(int id) {
        try {
            con = getConnection();
            PreparedStatement stmt = con.prepareStatement("delete from users.Employee where EmpId =" + id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public String edit(int id) {
        User user = null;
        System.out.println(id);

        try {
            con = getConnection();
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from users.Employee where EmpId = " + (id));
            rs.next();
            user = new User();
            user.setId(rs.getString("EmpId"));
            user.setFname(rs.getString("firstname"));
            user.setLname(rs.getString("lastname"));
            user.setAge(rs.getString("age"));
            user.setGender(rs.getString("Gender"));
            user.setDepartment(rs.getString("Department"));
            user.setMarital(rs.getString("Marital"));
            user.setAddress(rs.getString("Address"));
            user.setPhone(rs.getString("Phone"));
            user.setEmail(rs.getString("Email"));

            sessionMap.put("editUser", user);

            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return "Update.xhtml?faces-redirect=true";
    }

    public String update(User u) {
        try {
            con = getConnection();
            PreparedStatement stmt = con.prepareStatement(
                    "update users.Employee set firstname=?,lastname=?,age=?,Gender=?,Department=?,Marital=?,Address=?,Phone=?,Email=? where EmpId= ?");

            stmt.setString(1, u.getFname());
            stmt.setString(2, u.getLname());
            stmt.setString(3, u.getAge());
            stmt.setString(4, u.getGender());
            stmt.setString(5, u.getDepartment());
            stmt.setString(6, u.getMarital());
            stmt.setString(7, u.getAddress());
            stmt.setString(8, u.getPhone());
            stmt.setString(9, u.getEmail());
            stmt.setString(10, u.getId());

            stmt.executeUpdate();

            con.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
        return "index.xhtml?faces-redirect=true";
    }

//    public void validateName(FacesContext fc, 	UIComponent c, Object value){
//   if ( ((String)value).contains("!")||((String)value).contains("@")||((String)value).contains("#")||((String)value).contains("$")||((String)value).contains("%")||((String)value).contains("&")||((String)value).contains("*"))throw new ValidatorException(
//   new FacesMessage("Name cannot contain special characters"));
//    }
}
