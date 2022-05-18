package io.timpac.shop.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.timpac.shop.member.domain.Member;
import io.timpac.shop.member.repository.MemberRepository;

@Controller
public class MemberController {
	@Autowired
	private MemberRepository memberRepository;
	
	@GetMapping("/member/{id}/edit")
	public String edit(@PathVariable("id") Member member, Model model) {
		model.addAttribute("member", member);
		return "member/memberForm";
	}
}
