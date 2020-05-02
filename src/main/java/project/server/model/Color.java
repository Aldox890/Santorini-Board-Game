package project.server.model;

public enum Color {
    RED,CYAN,YELLOW;

    public String getColor(){
        if(this.equals(RED)){
            return "\u001B[31m";
        }
        if (this.equals(CYAN)){
            return "\u001B[36m";
        }
        if(this.equals(YELLOW)){
            return "\u001B[33m";
        }
        return "";
    }
}


