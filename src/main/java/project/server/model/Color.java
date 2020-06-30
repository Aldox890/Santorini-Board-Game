package project.server.model;

public enum Color {
    RED,CYAN,YELLOW;

    public String getColor(){
        if(this.equals(RED)){
            return "\033[31m";
        }
        if (this.equals(CYAN)){
            return "\033[36m";
        }
        if(this.equals(YELLOW)){
            return "\033[33m";
        }
        return "";
    }
}


