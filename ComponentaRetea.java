package Tema1;

public abstract class ComponentaRetea {
    private String id;
    private boolean statusOperational;

    public ComponentaRetea(String id){
        this.id = id;
        this.statusOperational = true;
    }

    public String getId(){
        return id;
    }

    public boolean isStatusOperational(){
        return statusOperational;
    }

    public void setStatusOperational(boolean statusOperational){
        this.statusOperational = statusOperational;
    }
}
