package coid.security.springsecurity.aopSecurity;

import coid.security.springsecurity.aopSecurity.pointcut.PointcutService;
import coid.security.springsecurity.dto.AccountDto;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AopSecurityController {

	@Autowired
	private AopMethodService aopMethodService;

	@Autowired
	private PointcutService pointcutService;

	@GetMapping("/preAuthorize")
	@PreAuthorize("hasRole('ROLE_USER') and #account.username == principal.username")
	public String preAuthorize(AccountDto account, Model model, Principal principal) {
		model.addAttribute("method", "success @PreAuthorize");
		return "aop/method";
	}

	@GetMapping("/methodSecured")
	public String methodSecured(Model model) {
		aopMethodService.methodSecured();
		model.addAttribute("method", "Success MethodSecured");
		return "aop/method";
	}

	@GetMapping("/pointcutSecured")
	public String pointcutSecured(Model model){
		pointcutService.notSecured();
		pointcutService.pointcutSecured();
		model.addAttribute("method", "Success MethodSecured");
		return "aop/method";
	}
}
