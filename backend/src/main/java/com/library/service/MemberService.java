package com.library.service;

import com.library.dto.MemberDto;
import com.library.model.Member;
import com.library.repository.MemberRepository;
import com.library.repository.TransactionRepository;
import com.library.model.TransactionStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;

    public MemberService(MemberRepository memberRepository, TransactionRepository transactionRepository) {
        this.memberRepository = memberRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public List<Member> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return memberRepository.findAll();
        }
        return memberRepository.search(query.trim());
    }

    public Member createMember(MemberDto dto) {
        Member member = new Member();
        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setMemberType(dto.getMemberType());
        return memberRepository.save(member);
    }

    public Member updateMember(Long id, MemberDto dto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + id));

        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setMemberType(dto.getMemberType());
        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + id));

        long activeLoans = transactionRepository.countActiveByMember(id);
        if (activeLoans > 0) {
            throw new IllegalStateException("Cannot delete member with " + activeLoans + " active loan(s)");
        }

        memberRepository.delete(member);
    }
}
