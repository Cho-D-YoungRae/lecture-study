package webservice;

import io.member.MemberRepository;
import io.member.impl.FileMemberRepository;
import was.httpserver.HttpServer;
import was.httpserver.HttpServlet;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.httpserver.servlet.anotation.AnnotationServletV3;

import java.io.IOException;
import java.util.List;

public class MemberServiceMain {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        MemberRepository memberRepository = new FileMemberRepository();
        MemberController memberController = new MemberController(memberRepository);
        HttpServlet servlet = new AnnotationServletV3(List.of(memberController));
        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(servlet);
        servletManager.add("favicon.ico", new DiscardServlet());

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }

}
