package com.portfolio.micaf.Controller;

import com.portfolio.micaf.Dto.dtoHys;
import com.portfolio.micaf.Entity.Hys;
import com.portfolio.micaf.Security.Controller.Mensaje;
import com.portfolio.micaf.Service.SHys;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hys")
@CrossOrigin(origins = "http://localhost:4200")
public class CHys {

    @Autowired
    SHys shys;

    @GetMapping("/lista")
    public ResponseEntity<List<Hys>> list() {
        List<Hys> list = shys.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Hys> getById(@PathVariable("id") int id) {
        if (!shys.existsById(id)) {
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        }
        Hys hys = shys.getOne(id).get();
        return new ResponseEntity(hys, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        if (!shys.existsById(id)) {
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        }
        shys.delete(id);

        return new ResponseEntity(new Mensaje("Skill eliminada"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody dtoHys dtohys) {
        if (StringUtils.isBlank(dtohys.getNombre())) {
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (shys.existsByNombre(dtohys.getNombre())) {
            return new ResponseEntity(new Mensaje("Esa skill existe"), HttpStatus.BAD_REQUEST);
        }

        Hys hys = new Hys(dtohys.getNombre(), dtohys.getPorcentaje());
        shys.save(hys);

        return new ResponseEntity(new Mensaje("Skill agregada"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody dtoHys dtohys) {
        //Validamos si existe el ID
        if (!shys.existsById(id)) {
            return new ResponseEntity(new Mensaje("El ID no existe"), HttpStatus.BAD_REQUEST);
        }
        //Compara nombre de experiencias
        if (shys.existsByNombre(dtohys.getNombre()) && shys.getByNombre(dtohys.getNombre()).get().getId() != id) {
            return new ResponseEntity(new Mensaje("Esa skill ya existe"), HttpStatus.BAD_REQUEST);
        }
        //No puede estar vacio
        if (StringUtils.isBlank(dtohys.getNombre())) {
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        Hys hys = shys.getOne(id).get();
        hys.setNombre(dtohys.getNombre());
        hys.setPorcentaje((dtohys.getPorcentaje()));

        shys.save(hys);
        return new ResponseEntity(new Mensaje("Skill actualizada"), HttpStatus.OK);

    }
}


