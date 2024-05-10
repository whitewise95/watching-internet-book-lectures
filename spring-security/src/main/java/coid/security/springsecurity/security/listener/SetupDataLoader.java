package coid.security.springsecurity.security.listener;

import coid.security.springsecurity.dmain.AccessIp;
import coid.security.springsecurity.dmain.Account;
import coid.security.springsecurity.dmain.Resources;
import coid.security.springsecurity.dmain.Role;
import coid.security.springsecurity.dmain.RoleHierarchy;
import coid.security.springsecurity.repository.AccessIpRepository;
import coid.security.springsecurity.repository.ResourcesRepository;
import coid.security.springsecurity.repository.RoleHierarchyRepository;
import coid.security.springsecurity.repository.RoleRepository;
import coid.security.springsecurity.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean alreadySetup = false;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final ResourcesRepository resourcesRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleHierarchyRepository roleHierarchyRepository;
	private final AccessIpRepository accessIpRepository;

	private static AtomicInteger count = new AtomicInteger(0);

	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {

		if (alreadySetup) {
			return;
		}

		setupSecurityResources();

		alreadySetup = true;
	}

	public void setupSecurityResources() {
		setupAccessIpData();

		Set<Role> roles = new HashSet<>();
		Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
		roles.add(adminRole);
		createResourceIfNotFound("/admin/**", "", roles, "url");
		Account account = createUserIfNotFound("admin", "pass", "admin@gmail.com", 10, roles);

		Set<Role> roles1 = new HashSet<>();


		Role managerRole = createRoleIfNotFound("ROLE_MANAGER", "매니저");
		roles1.add(managerRole);

		Set<Role> roles3 = new HashSet<>();

		Role childRole1 = createRoleIfNotFound("ROLE_USER", "회원");
		roles3.add(childRole1);
		createResourceIfNotFound("/users/**", "", roles3, "url");
		createUserIfNotFound("user", "pass", "user@gmail.com", 30, roles3);
		createRoleHierarchyIfNotFound(childRole1, managerRole);
		createRoleHierarchyIfNotFound(managerRole, adminRole);

		createResourceIfNotFound("coid.security.springsecurity.aopSecurity.AopMethodService.methodSecured", "", roles3, "method");
		// createResourceIfNotFound("coid.security.springsecurity.aopsecurity.method.AopMethodService.innerCallMethodTest", "", roles1, "method");
		 createResourceIfNotFound("execution(* coid.security.springsecurity.aopSecurity.pointcut.*Service.pointcut*(..))", "", roles1, "pointcut");
		// createUserIfNotFound("manager", "pass", "manager@gmail.com", 20, roles1);

	}

	@Transactional
	public Role createRoleIfNotFound(String roleName, String roleDesc) {

		Role role = roleRepository.findByRoleName(roleName);

		if (role == null) {
			role = Role.builder()
					   .roleName(roleName)
					   .roleDesc(roleDesc)
					   .build();
		}
		return roleRepository.save(role);
	}

	@Transactional
	public Account createUserIfNotFound(String userName, String password, String email, int age, Set<Role> roleSet) {

		Account account = userRepository.findByUsername(userName);

		if (account == null) {
			account = Account.builder()
							 .username(userName)
							 .email(email)
							 .age(age)
							 .password(passwordEncoder.encode(password))
							 .userRoles(roleSet)
							 .build();
		}
		return userRepository.save(account);
	}

	@Transactional
	public Resources createResourceIfNotFound(String resourceName, String httpMethod, Set<Role> roleSet, String resourceType) {
		Resources resources = resourcesRepository.findByResourceNameAndHttpMethod(resourceName, httpMethod);

		if (resources == null) {
			resources = Resources.builder()
								 .resourceName(resourceName)
								 .roleSet(roleSet)
								 .httpMethod(httpMethod)
								 .resourceType(resourceType)
								 .orderNum(count.incrementAndGet())
								 .build();
		}
		return resourcesRepository.save(resources);
	}

	@Transactional
	public void createRoleHierarchyIfNotFound(Role childRole, Role parentRole) {

		RoleHierarchy roleHierarchy = roleHierarchyRepository.findByChildName(parentRole.getRoleName());
		if (roleHierarchy == null) {
			roleHierarchy = RoleHierarchy.builder()
										 .childName(parentRole.getRoleName())
										 .build();
		}
		RoleHierarchy parentRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);

		roleHierarchy = roleHierarchyRepository.findByChildName(childRole.getRoleName());
		if (roleHierarchy == null) {
			roleHierarchy = RoleHierarchy.builder()
										 .childName(childRole.getRoleName())
										 .build();
		}

		RoleHierarchy childRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);
		childRoleHierarchy.setParentName(parentRoleHierarchy);
	}

	private void setupAccessIpData() {
		AccessIp byIpAddress = accessIpRepository.findByIpAddress("127.0.0.1");
		if (byIpAddress == null) {
			AccessIp accessIp = AccessIp.builder()
										.ipAddress("127.0.0.1")
										.build();
			accessIpRepository.save(accessIp);
		}
	}
}