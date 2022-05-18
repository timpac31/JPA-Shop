package io.timpac.shop.member.repository;

import java.util.List;

import io.timpac.shop.member.domain.Member;

public interface MemberRepositoryCustom {
	List<Member> findByCustom();
}
