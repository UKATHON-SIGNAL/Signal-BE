package com.signal.signalbe.domain.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.signal.signalbe.domain.user.User;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_interests", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "topic_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UserInterest(User user, Topic topic) {
        this.user = user;
        this.topic = topic;
        this.createdAt = LocalDateTime.now();
    }
}
