package org.mybatis.generator.codegen.mybatis3.model;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;

/**
* @Description: Dto Vo 等自定义自动生成实现
* @author wangheng
* @date 2018年3月29日 下午3:23:54
*/
public class CustomGenerator extends AbstractJavaGenerator {

    public CustomGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.6", table.toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();
        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();

        answer.add(dtoGenerator(commentGenerator));
        answer.add(VoGenerator(commentGenerator));
        answer.add(ServiceGenerator(commentGenerator));
        answer.add(ServiceImplGenerator(commentGenerator));

        return answer;
    }

    private TopLevelClass dtoGenerator(CommentGenerator commentGenerator) {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType() + "Dto");
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        topLevelClass.setSuperClass(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        topLevelClass.addImportedType(introspectedTable.getBaseRecordType());

        return topLevelClass;
    }

    private TopLevelClass VoGenerator(CommentGenerator commentGenerator) {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType() + "Vo");
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        topLevelClass.setSuperClass(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        topLevelClass.addImportedType(introspectedTable.getBaseRecordType());

        return topLevelClass;
    }

    private Interface ServiceGenerator(CommentGenerator commentGenerator) {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                introspectedTable.getBaseRecordType().replace("entity", "service") + "Service");
        Interface inf = new Interface(type);
        inf.setVisibility(JavaVisibility.PUBLIC);

        commentGenerator.addJavaFileComment(inf);

        return inf;
    }

    private TopLevelClass ServiceImplGenerator(CommentGenerator commentGenerator) {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                introspectedTable.getBaseRecordType().replace("entity", "service.impl") + "ServiceImpl");
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        topLevelClass.addSuperInterface(new FullyQualifiedJavaType(
                introspectedTable.getFullyQualifiedTable().getDomainObjectName() + "Service"));
        topLevelClass.addImportedType(introspectedTable.getBaseRecordType().replace("entity", "service") + "Service");
        topLevelClass.addImportedType("org.springframework.stereotype.Service");

        topLevelClass.addAnnotation("@Service");

        topLevelClass.addImportedType("org.springframework.beans.factory.annotation.Autowired");
        topLevelClass.addImportedType(introspectedTable.getDAOInterfaceType());

        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);

        String daoFieldName = introspectedTable.getFullyQualifiedTable().getDomainObjectName().substring(0, 1)
                .toUpperCase() + introspectedTable.getFullyQualifiedTable().getDomainObjectName().substring(1) + "Dao";
        String daoFieldType = introspectedTable.getFullyQualifiedTable().getDomainObjectName() + "Dao";
        field.addAnnotation("@Autowired");
        field.setType(new FullyQualifiedJavaType(daoFieldType));
        field.setName(daoFieldName);
        topLevelClass.addField(field);

        // 添加方法
        Method method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setName("getDao");
        method.setReturnType(new FullyQualifiedJavaType(daoFieldType));
        method.addBodyLine("return this." + daoFieldName + ";");

        topLevelClass.addMethod(method);

        return topLevelClass;
    }

}
