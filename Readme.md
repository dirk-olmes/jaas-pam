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