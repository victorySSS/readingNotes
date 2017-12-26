class User{
    private String userName ;
    private String userPassword;

    User(String name, String password){
        userName = name;
        userPassword = password;
    }


    String getName(){
        return userName;
    }

    boolean isMatch(String password){
        return password.equals(userPassword);
    }
}