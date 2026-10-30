package pt.seixal.carlos.exceptions.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.Date;

@JacksonXmlRootElement(localName = "ExceptionResponse")
public class ExceptionResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ExceptionResponse(int status, String error, String message, String path) {
        this.timestamp = new Date(); // O java.util.Date é serializado perfeitamente pelo Jackson XML por padrão
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Getters obrigatórios para o Jackson conseguir ler os campos
    public Date getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
}
