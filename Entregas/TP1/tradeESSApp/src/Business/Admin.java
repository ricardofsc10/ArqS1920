package Business;

public class Admin extends User {

    public Admin() {}

    public Admin(int id, String email, String username, String password, String morada, int idade, int contacto) {
        super(id, email, username, password, morada, idade, contacto);
    }

    public Admin(Admin admin){
        super(admin.getId(), admin.getEmail(), admin.getUsername(), admin.getPassword(), admin.getMorada(), admin.getIdade(), admin.getContacto());
    }

    public Admin clone(){
        return new Admin(this);
    }

    public boolean equals(Object object){
        if(object==this) return true;

        if(object==null || object.getClass() != getClass()) return false;

        Admin admin = (Admin) object;

        return getId() == admin.getId();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("---- Administrador ----\nID : ")
                .append(this.getId())
                .append("\nEmail : ")
                .append(this.getEmail())
                .append("\nNome : ")
                .append(this.getUsername())
                .append("\n------------------\n");
        return sb.toString();
    }
}
