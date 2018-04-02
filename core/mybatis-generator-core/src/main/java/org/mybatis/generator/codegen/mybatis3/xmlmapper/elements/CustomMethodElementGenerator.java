package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
* @Description: 自定义dao方法xml映射
* @author wangheng
* @date 2018年4月2日 上午11:09:53
*/
public class CustomMethodElementGenerator extends AbstractXmlElementGenerator {

    protected static final String Sql_Select_Condition = "Sql_Select_Condition";

    protected static final String Sql_Update_Condition = "Sql_Update_Condition";

    @Override
    public void addElements(XmlElement parentElement) {
        this.addSql_Select_Condition(parentElement);
        parentElement.addElement(new TextElement(""));

        this.addSelectByCondition(parentElement);
        parentElement.addElement(new TextElement(""));

        this.addCountByCondition(parentElement);
        parentElement.addElement(new TextElement(""));

        this.addSql_Update_Condition(parentElement);
        parentElement.addElement(new TextElement(""));

        this.addUpdateByCondition(parentElement);
        parentElement.addElement(new TextElement(""));
        System.out.println("自定义dao方法，xml生成完毕！");
    }

    private void addSql_Select_Condition(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", Sql_Select_Condition));

        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "_parameter != null"));

        XmlElement whereElement = new XmlElement("where");
        whereElement.addElement(new TextElement("1=1"));

        StringBuilder sb = new StringBuilder();

        for (IntrospectedColumn introspectedColumn : ListUtilities
                .removeGeneratedAlwaysColumns(introspectedTable.getAllColumns())) {
            sb.setLength(0);
            XmlElement isNotNullElement = new XmlElement("if");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null");
            isNotNullElement.addAttribute(new Attribute("test", sb.toString()));

            sb.setLength(0);
            sb.append("AND ");
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));

            isNotNullElement.addElement(new TextElement(sb.toString()));

            whereElement.addElement(isNotNullElement);
        }

        ifElement.addElement(whereElement);
        answer.addElement(ifElement);
        parentElement.addElement(answer);
    }

    private void addSelectByCondition(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "selectByCondition"));
        answer.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType() + "Dto"));
        answer.addAttribute(new Attribute("resultType", introspectedTable.getBaseRecordType() + "Vo"));

        answer.addElement(new TextElement("select"));
        XmlElement includeE = new XmlElement("include");
        includeE.addAttribute(new Attribute("refid", introspectedTable.getBaseColumnListId()));
        answer.addElement(includeE);
        answer.addElement(new TextElement("from " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));

        XmlElement includeSql = new XmlElement("include");
        includeSql.addAttribute(new Attribute("refid", Sql_Select_Condition));
        answer.addElement(includeSql);

        parentElement.addElement(answer);
    }

    private void addCountByCondition(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "countByCondition"));
        answer.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType() + "Dto"));
        answer.addAttribute(new Attribute("resultType", "java.lang.Long"));

        answer.addElement(new TextElement(
                "select count(*) from " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));

        XmlElement includeSql = new XmlElement("include");
        includeSql.addAttribute(new Attribute("refid", Sql_Select_Condition));
        answer.addElement(includeSql);

        parentElement.addElement(answer);
    }

    private void addSql_Update_Condition(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", Sql_Update_Condition));

        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "_parameter != null and _parameter.condition != null"));

        XmlElement whereElement = new XmlElement("where");
        whereElement.addElement(new TextElement("1=1"));

        StringBuilder sb = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : ListUtilities
                .removeGeneratedAlwaysColumns(introspectedTable.getAllColumns())) {
            sb.setLength(0);
            XmlElement isNotNullElement = new XmlElement("if");
            sb.append(introspectedColumn.getJavaProperty("condition."));
            sb.append(" != null");
            isNotNullElement.addAttribute(new Attribute("test", sb.toString()));

            sb.setLength(0);
            sb.append("AND ");
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "condition."));

            isNotNullElement.addElement(new TextElement(sb.toString()));

            whereElement.addElement(isNotNullElement);
        }

        ifElement.addElement(whereElement);
        answer.addElement(ifElement);
        parentElement.addElement(answer);
    }

    public void addUpdateByCondition(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update");

        answer.addAttribute(new Attribute("id", "updateByCondition"));
        answer.addAttribute(new Attribute("parameterType", "map"));

        context.getCommentGenerator().addComment(answer);

        answer.addElement(new TextElement("update " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));

        XmlElement setElement = new XmlElement("set");

        StringBuilder sb = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : ListUtilities
                .removeGeneratedAlwaysColumns(introspectedTable.getAllColumns())) {
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty("record."));
            sb.append(" != null");
            XmlElement isNotNullElement = new XmlElement("if");
            isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
            setElement.addElement(isNotNullElement);

            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record."));
            sb.append(',');

            isNotNullElement.addElement(new TextElement(sb.toString()));
        }
        answer.addElement(setElement);

        XmlElement includeSql = new XmlElement("include");
        includeSql.addAttribute(new Attribute("refid", Sql_Update_Condition));

        answer.addElement(includeSql);

        parentElement.addElement(answer);
    }
}
