JAAS LoginModule using PAM
==========================

This is a JAAS LoginModule that uses PAM (via [libpam4j](https://github.com/kohsuke/libpam4j)). 

Due to [limitations in the pam stack](http://jenkins-ci.361315.n4.nabble.com/Using-UNIX-PAM-authentication-from-a-non-root-user-td378559.html#a378563) it is not very useful, though.

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