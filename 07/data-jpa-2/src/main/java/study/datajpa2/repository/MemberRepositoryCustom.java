package study.datajpa2.repository;

import study.datajpa2.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> findMemberCustom();
}
