package hello.servlet.web.servlet;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "memberServlet", urlPatterns = "/servlet/members")
public class MemberListServlet extends HttpServlet {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        List<Member> members = memberRepository.findAll();

        res.setContentType("text/html");
        res.setCharacterEncoding("utf-8");

        PrintWriter w = res.getWriter();

        w.write("<html>\n");
        w.write("<head>\n");
        w.write("    <meta charset=\"UTF-8\">\n");
        w.write("    <title>Title</title>\n");
        w.write("</head>\n");
        w.write("<body>\n");
        w.write("<a href=\"/index.html\">메인</a>\n");
        w.write("<table>\n");
        w.write("    <thead>\n");
        w.write("    <th>id</th>\n");
        w.write("    <th>username</th>\n");
        w.write("    <th>age</th>\n");
        w.write("    </thead>\n");
        w.write("    <tbody>\n");

        for (Member member : members) {
            w.write("   <tr>\n");
            w.write("       <td>" + member.getId() + "</td>\n");
            w.write("       <td>" + member.getUsername() + "</td>\n");
            w.write("       <td>" + member.getAge() + "</td>\n");
        }

        w.write("    </tbody>\n");
        w.write("</table>\n");
        w.write("</body>\n");
        w.write("</html>\n");
    }
}
