package io.timpac.shop.member.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.timpac.shop.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String>, JpaSpecificationExecutor<Member>, MemberRepositoryCustom {
	
	List<Member> findByUsernameAndAge(String username, Integer age);
	
	@Query("select m from Member m where m.createDate > :createDate")
	List<Member> findByCreateDateAfter(@Param("createDate") Date createDate);
	
	@Modifying(clearAutomatically = true)
	@Query("update Member m set m.age = m.age + 1")
	int bulkPriceUp();
	
	Page<Member> findByUsernameStartingWith(String name, Pageable pageable);
	
	List<Member> findAll(Specification<Member> spec);
}
