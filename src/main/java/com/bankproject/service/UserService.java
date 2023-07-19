package com.bankproject.service;

import com.bankproject.constants.Errors;
import com.bankproject.constants.MailConstants;
import com.bankproject.dto.UserDTO;
import com.bankproject.dto.RoleDTO;
import com.bankproject.exception.MyDeleteException;
import com.bankproject.mapper.GenericMapper;
import com.bankproject.utils.MailUtils;
import com.bankproject.model.User;
import com.bankproject.repository.GenericRepository;
import com.bankproject.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class UserService extends GenericService<User, UserDTO> {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSender javaMailSender;

    public UserService(GenericRepository<User> repository,
                       GenericMapper<User, UserDTO> mapper,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       JavaMailSender javaMailSender) {
        super(repository, mapper);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public UserDTO create(UserDTO newObject) {
        if (Objects.isNull(newObject.getRole())) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(1L);
            newObject.setRole(roleDTO);
        }
        newObject.setPassword(bCryptPasswordEncoder.encode(newObject.getPassword()));
        newObject.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        newObject.setCreatedWhen(LocalDateTime.now());
        return mapper.toDTO(repository.save(mapper.toEntity(newObject)));
    }

    public void changeRole(Long id) {
        UserDTO userDTO = getOne(id);
        Long roleId = userDTO.getRole().getId();
        if(roleId == 1L) {
            userDTO.getRole().setId(2L);
        } else {
            userDTO.getRole().setId(1L);
        }
        update(userDTO);
    }

    @Override
    public void deleteSoft(Long objectId) throws MyDeleteException {
        User user = repository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Клиента с заданным id=" + objectId + " не существует."));

        boolean userCanBeDeleted = ((UserRepository) repository).checkUserForDeletion(objectId);

        if (userCanBeDeleted) {
            markAsDeleted(user);
            repository.save(user);
        }
        else {
            throw new MyDeleteException(Errors.User.USER_DELETE_ERROR);
        }
    }

    public UserDTO getUserByLogin(final String login) {
        return mapper.toDTO(((UserRepository) repository).findUserByLogin(login));
    }

    public UserDTO getUserByEmail(final String email) {
        return mapper.toDTO(((UserRepository) repository).findUserByEmail(email));
    }

    public void sendChangePasswordEmail(final UserDTO userDTO) {
        UUID uuid = UUID.randomUUID();
        log.info("Generated Token: {} ", uuid);

        userDTO.setChangePasswordToken(uuid.toString());
        update(userDTO);

        SimpleMailMessage mailMessage = MailUtils.createMailMessage(
                userDTO.getEmail(),
                MailConstants.MAIL_SUBJECT_FOR_REMEMBER_PASSWORD,
                MailConstants.MAIL_MESSAGE_FOR_REMEMBER_PASSWORD + uuid
        );

        javaMailSender.send(mailMessage);
    }

    public void changePassword(String uuid, String password) {
        UserDTO userDTO = mapper.toDTO(((UserRepository) repository).findUserByChangePasswordToken(uuid));
        userDTO.setChangePasswordToken(null);
        userDTO.setPassword(bCryptPasswordEncoder.encode(password));
        update(userDTO);
    }

    public Page<UserDTO> findUsers(UserDTO userDTO,
                                   Pageable pageable) {
        Page<User> users = ((UserRepository) repository).searchUsers(userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getLogin(),
                pageable);
        List<UserDTO> result = mapper.toDTOs(users.getContent());
        return new PageImpl<>(result, pageable, users.getTotalElements());
    }
//
//    public UserDTO getUserByLastNameAndFirstName(final String lastName,
//                                                 final String firstName) {
//        return mapper.toDTO(((UserRepository) repository).findUserByLastNameAndFirstName(lastName, firstName));
//    }
}
