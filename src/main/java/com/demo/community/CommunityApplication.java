package com.demo.community;

import com.demo.community.account.entity.Account;
import com.demo.community.account.role.AccountRole;
import com.demo.community.account.service.AccountService;
import com.demo.community.board.repository.BoardRepository;
import com.demo.community.board.repository.CommentRepository;
import com.demo.community.board.entity.Board;
import com.demo.community.board.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@SpringBootApplication
public class CommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

	@Bean
	public ApplicationRunner applicationRunner() {
		return new ApplicationRunner() {

			@Autowired
			AccountService accountService;

			@Autowired
			BoardRepository boardRepository;

			@Autowired
			CommentRepository commentRepository;

			@Override
			public void run(ApplicationArguments args) {
				Account user = Account.builder()
						.email("user@user.com")
						.password("user")
						.nickname("user")
						.roles(Set.of(AccountRole.USER))
						.build();
				accountService.createAccount(user);

				Account specialist = Account.builder()
						.email("specialist@specialist.com")
						.password("specialist")
						.nickname("specialist")
						.roles(Set.of(AccountRole.SPECIALIST))
						.build();
				accountService.createAccount(specialist);

				Account admin = Account.builder()
						.email("admin@admin.com")
						.password("admin")
						.nickname("admin")
						.roles(Set.of(AccountRole.ADMIN))
						.build();
				accountService.createAccount(admin);

				Account other = Account.builder()
						.email("other@other.com")
						.password("other")
						.nickname("other")
						.roles(Set.of(AccountRole.OTHER))
						.build();
				accountService.createAccount(other);

				for(int i = 0; i < 12; i++) {
					int num = i + 1;
					boardRepository.save(Board.builder()
							.title("Board " + num)
							.content("Board " + num)
							.author(user)
							.build());
				}

				Board board1 = boardRepository.findById(1L).get();
				for(int i = 0; i < 12; i++) {
					int num = i + 1;
					commentRepository.save(Comment.builder()
							.board(board1)
							.content("Comment " + num)
							.author(user)
							.build());
				}

			}
		};
	}

}
