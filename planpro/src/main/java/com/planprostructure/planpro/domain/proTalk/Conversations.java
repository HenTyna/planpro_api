package com.planprostructure.planpro.domain.proTalk;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_conversations")
public class Conversations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id")
    private Long id;

    @Column(name = "is_grp")
    private boolean isGroup;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "conversation_members",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "usr_id")
    )
    private Set<UserChat> members = new HashSet<>();

    @OneToMany(
            mappedBy = "conversation",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Messages> messages = new ArrayList<>();

    @Builder
    public Conversations(Long id, boolean isGroup, String name, Set<UserChat> members, List<Messages> messages) {
        this.id = id;
        this.isGroup = isGroup;
        this.name = name;
        this.members = members != null ? members : new HashSet<>();
        this.messages = messages != null ? messages : new ArrayList<>();
    }
}