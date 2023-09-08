package com.company.youse.services;

import com.company.youse.repositrories.SessionRepository;
import com.company.youse.repositrories.UserRepository;
import com.company.youse.errorHandler.BadRequestException;
import com.company.youse.errorHandler.ServerErrorException;
import com.company.youse.models.Session;
import com.company.youse.models.User;
import com.company.youse.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    public boolean isTokenExist(String jwt) {
        return true;
    }

    /**
     * method does not use session table. it instead uses userDao object to return a user
     * @param email
     * @return
     */
    public User getUserFromToken(String email) {

        return userRepository.findUserByEmail(email);

    }

    public boolean isValidRefreshToken(String refreshToken) {
        if(sessionRepository.findFirstByRefreshToken(refreshToken) != null){
            return true;
        }
        return false;
    }

    /**
     * generates a refresh token for the provided user
     * @param user
     * @return
     */
    public Session generateRefreshToken(User user){
        String token = jwtProvider.createRefreshToken(user.getEmail());
        Session session = new Session(token, user);
        sessionRepository.save(session);
        System.out.println("created session");
        return session;
    }

    /**
     * renews a refresh token and updates it in database
     * @param user
     * @param refresh_token
     * @return
     */
    public String renewRefreshToken(User user, String refresh_token){
        if(sessionRepository.findFirstByRefreshToken(refresh_token)!= null){
            String token = jwtProvider.createRefreshToken(user.getEmail());
            Session session = sessionRepository.findFirstByRefreshToken(refresh_token);
            session.setRefreshToken(token);
            sessionRepository.save(session);
            return token;
        }else{
            throw new BadRequestException("invalid request");
        }
    }

    /**
     * method deletes all sessions for a current user
     * @param user
     */
    public void removeAllSessionsFor(User user) {
        // if deletion is not successful, throw an 500 error
        sessionRepository.deleteAllByUser(user);
        if(!sessionRepository.findAllByUser(user).isEmpty()) {
            throw new ServerErrorException("An error occurred, contact support");
        }
    }

    /**
     * method removes a session with a certain refresh token
     * @param refreshToken
     * @return true if session has been removed successfully else returns a false
     */
    public boolean hasRemovedSessionWith(String refreshToken) {
        // try retrieving a session with the refresh token in database
        Session session = sessionRepository.findFirstByRefreshToken(refreshToken);
        if(session != null) {
            //if session is not null, remove the session from database
            sessionRepository.delete(session);
            // try retrieving the session with the refresh token once again in database
            session = sessionRepository.findFirstByRefreshToken(refreshToken);
            // If session is null this time, user has been logged out and can return true, else an error occurred and user has not been logged out, should return false
            if (session == null)
                return true;
            else
                return false;
        }
        // session is null, either refresh token is invalid or user had not logged in, throw an error instead
        throw new BadRequestException("invalid session");
    }


    /**
     * method removes a session with a certain refresh token
     * @param id
     * @return true if session has been removed successfully else returns a false
     */
    public boolean hasRemovedSessionWithId(Long id, User user) {
        // try retrieving a session with the refresh token in database
        Session session = sessionRepository.findById(id).get();
        //if session is not null, remove the session from database
        if(session != null) {
            // confirm is user owns the session
            if(session.getUser().getId() != user.getId()){
                return false;
            }
            sessionRepository.delete(session);
            // try retrieving the session with the refresh token once again in database
            session = sessionRepository.findById(id).get();
            // If session is null this time, user has been logged out and can return true, else an error occurred and user has not been logged out, should return false
            if (session == null)
                return true;
            else
                return false;
        }
        // session is null, either refresh token is invalid or user had not logged in, throw an error instead
        return false;
    }
}
