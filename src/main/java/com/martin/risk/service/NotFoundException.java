package com.martin.risk.service;

// Thrown when a payment doesn't exist OR belongs to another organization -> HTTP 404
public class NotFoundException extends RuntimeException {
}
