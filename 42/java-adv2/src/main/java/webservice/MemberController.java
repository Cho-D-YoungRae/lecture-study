package webservice;

import io.member.Member;
import io.member.MemberRepository;
import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.servlet.anotation.Mapping;

import java.util.List;

import static util.MyLogger.log;

public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Mapping("/")
    public void home(HttpResponse response) {
        response.writeBody("""
                <html>
                <body>
                <h1>Member Manager</h1>
                <ul>
                  <li><a href='/members'>Member List</a></li>
                  <li><a href='/add-member-form'>Add New Member</a></li>
                </ul>
                </body>
                </html>
                """);
    }

    @Mapping("/members")
    public void members(HttpResponse response) {
        List<Member> members = memberRepository.findAll();
        StringBuilder page = new StringBuilder();
        page.append("<html><body>");
        page.append("<h1>Member List</h1>");
        page.append("<ul>");
        for (Member member : members) {
            page.append("<li>")
                    .append("ID: ").append(member.getId())
                    .append(", Name: ").append(member.getName())
                    .append(", Age: ").append(member.getAge())
                    .append("</li>");
        }
        page.append("</ul>");
        page.append("<a href='/'>Back to Home</a>");
        page.append("</body></html>");
        response.writeBody(page.toString());
    }

    @Mapping("/add-member-form")
    public void addMemberForm(HttpResponse response) {
        String body = """
                <html>
                <body>
                <h1>Add New Member</h1>
                <form method='POST' action='/add-member'>
                ID: <input type='text' name='id'><br>
                Name: <input type='text' name='name'><br>
                Age: <input type='text' name='age'><br>
                <input type='submit' value='Add'>
                </form>
                <a href='/'>Back to Home</a>
                </body>
                </html>
                """;
        response.writeBody(body);
    }

    @Mapping("/add-member")
    public void addMember(HttpRequest request, HttpResponse response) {
        log("MemberController.addMember");
        log("request=" + request);

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(id, name, age);
        memberRepository.add(member);

        response.writeBody("<h1>Save OK</h1>");
        response.writeBody("<a href='/'>Back to Home</a>");
    }
}
