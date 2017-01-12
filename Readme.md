JAAS LoginModule using PAM
==========================

This is a JAAS LoginModule that uses PAM (via [libpam4j](https://github.com/kohsuke/libpam4j)). 

Usage
-----

Create a login.conf containing:

    pam-login {
        de.codedo.jaas.PamLoginModule required
        service = sshd;
    };
    
and the activate the config passing

    -Djava.security.auth.login.config=login.cfg
    
on the commandline.

Implementation Notes
--------------------

After successful login, the module will put a `PamPrincipal` instance into the Subject that's used for Authentication. This principal will contain all the info that the pam login returned. 

Caveats with pam_unix Authentication
------------------------------------

When a process running as non-root user tries to autheticate via `pam_unix.so` it invokes the `unix_chkpwd` helper binary. This helper program changes its effective uid to the user that's running the binary. Since this user typically does not have access to the `/etc/shadow` file [only the currently logged in user can be authenticated](http://jenkins-ci.361315.n4.nabble.com/Using-UNIX-PAM-authentication-from-a-non-root-user-td378559.html#a378563). The `/etc/shadow` file is typically owned by root. Some Linux distributions (e.g. [Debian](http://www.debian.org)) use a special group to govern access to this file. In that case you'd have to add the user that runs the process to the respective group to make authentication work for other users.

References:
 - [1](http://jenkins-ci.361315.n4.nabble.com/Using-UNIX-PAM-authentication-from-a-non-root-user-td378559.html#a378563)
 - [2](https://groups.google.com/d/msg/rundeck-discuss/fTg_S6dCvUw/7Ic19l5k-8gJ)
 - [3](https://github.com/canweriotnow/rpam-ruby19/issues/5#issuecomment-14255002) 
 - [4](stackoverflow.com/questions/5286321/pam-authentication-in-python-without-root-privileges#5291263)
