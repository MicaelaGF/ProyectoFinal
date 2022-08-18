
package com.portfolio.micaf.Security.Entity;

import com.portfolio.micaf.Security.Enums.RolNombre;
import javax.persistence.Entity;

@Entity
public class Rol {
    private int id;
    private RolNombre rolNombre;
}
