package sg.gov.moh.iais.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ERRORMESSAGE")
public class Message {
    public static final String TABLE_NAME = "ERRORMESSAGE";
    public static final String COL_ID = "CODE_ID";
    public static final String COL_CODE_KEY = "CODE_KEY";
    public static final String COL_TYPE = "TYPE";
    public static final String COL_MESSAGE_TYPE = "MESSAGE_TYPE";
    public static final String COL_MODULE = "MODULE";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_STATUS = "STATUS";

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer codeId;


    @Setter
    @Getter
    @Column(name="CODE_KEY")
    private String codeKey;

    @Setter
    @Getter
    @Column(name="TYPE")
    private String type;

    @Setter
    @Getter
    @Column(name = "MESSAGE_TYPE")
    private String message_type;

    @Setter
    @Getter
    @Column(name="MODULE")
    private String module;

    @Setter
    @Getter
    @Column(name="DESCRIPTION")
    private String description;

    @Setter
    @Getter
    @Column(name="STATUS")
    private String status;
}
