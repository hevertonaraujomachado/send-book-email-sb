package com.devsuperior.send_book_email_sb.step;

import com.devsuperior.send_book_email_sb.domain.UserBookLoan;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import com.sendgrid.helpers.mail.Mail;


@Configuration
public class SendEmailUserStepConfig {

    @Autowired
    @Qualifier("transactionManagerApp")
    private PlatformTransactionManager transactionManager;

    @Bean
    public Step sendEmailUserStep(
            ItemReader<UserBookLoan> readUsersWithLoansCloseToReturnReader,
            ItemProcessor<UserBookLoan, Mail> processLoanNotificationEmailProcessor,
            ItemWriter<Mail> sendEmailRequestWriter,
            JobRepository jobRepository) {

        return new StepBuilder("sendEmailUserStep", jobRepository)
                .<UserBookLoan, Mail>chunk(1)
                .transactionManager(transactionManager)
                .reader(readUsersWithLoansCloseToReturnReader)
                .processor(processLoanNotificationEmailProcessor)
                .writer(sendEmailRequestWriter)
                .build();
    }
}