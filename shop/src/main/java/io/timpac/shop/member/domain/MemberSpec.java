package io.timpac.shop.member.domain;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class MemberSpec {
	
	public static Specification<Member> teamName(final String teamName) {
		return new Specification<Member>() {
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if(!StringUtils.hasText(teamName)) return null;
				
				Join<Member, Team> t = root.join("team", JoinType.INNER);
				return builder.equal(t.get("name"), teamName);
			}
		};
	}
	
}
