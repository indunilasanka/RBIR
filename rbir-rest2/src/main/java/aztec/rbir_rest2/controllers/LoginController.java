package aztec.rbir_rest2.controllers;

import org.springframework.web.bind.annotation.RestController;
import aztec.rbir_database.Entities.User;
import aztec.rbir_database.service.UserDataService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;


@RestController
@RequestMapping(value = "/user")
public class LoginController {
	
    @Autowired
    private TokenStore tokenStore;
    
    @Autowired
    private UserDataService userDataService;
    
    
    
    private ApprovalStore approvalStore;
    

    @Autowired
    private ConsumerTokenServices consumerTokenServices;
	
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization",
        value = "Bearer access_token",
        required = true,
        dataType = "string",
        paramType = "header"),
    })
	@RequestMapping(value = "/getProfile", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('ADMIN_USER')")
	protected User register(@RequestParam("username") String username) {
    	return userDataService.retrieveFromUserName(username);
	}
    
    
    @RequestMapping(value = "/getRankedOfficers", method = RequestMethod.GET)
	protected List<User> getRankedOfficers(@RequestParam("documentlevel") String level) {
    	return userDataService.retrieveUsersForConfirmation(level);
	}

    
    
    /*
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization",
        value = "Bearer access_token",
        required = true,
        dataType = "string",
        paramType = "header"),
    })
    @RequestMapping(value = "/oauth/revoke-token", method = RequestMethod.GET)
    //@ResponseStatus(HttpStatus.OK)
    public void removeAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
            
            consumerTokenServices.revokeToken(tokenValue);
        }
    }

    @RequestMapping(value = "/oauth/revoke-refresh-token", method = RequestMethod.GET)
    //@ResponseStatus(HttpStatus.OK)
    public void removeRefreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(tokenValue);
            tokenStore.removeRefreshToken(refreshToken);

        }
    }
    */
}
