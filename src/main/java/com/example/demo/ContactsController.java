package com.example.demo;

import ch.qos.logback.core.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController("/contacts")
public class ContactsController {

    public static final List<Contact> contacts = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Void> createContact(@RequestBody final Contact contactToAdd) {
        if (StringUtil.isNullOrEmpty(contactToAdd.getPhone())) {
            return ResponseEntity.badRequest().build();
        }
        if (StringUtil.isNullOrEmpty(contactToAdd.getName())) {
            return ResponseEntity.badRequest().build();
        }
        contacts.add(contactToAdd);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Contact> getContacts() {
        return contacts;
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<Void> editContact(@PathVariable String contactId, @RequestBody final Contact updateBody) {
        HttpStatusCode httpStatusCode = HttpStatusCode.valueOf(200);
        final Optional<Contact> contact = contacts.stream()
                .filter(c -> c.getId().equals(contactId))
                .findFirst();
        if (contact.isPresent()) {
            contact.get().setEmail(updateBody.getEmail());
            contact.get().setName(updateBody.getName());
            contact.get().setNote(updateBody.getNote());
            contact.get().setPhone(updateBody.getPhone());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable final String contactId) {
        final Optional<Contact> contact = contacts.stream().filter(c -> c.getId().equals(contactId))
                .findFirst();

        if (contact.isPresent()) {
            contacts.remove(contact.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
