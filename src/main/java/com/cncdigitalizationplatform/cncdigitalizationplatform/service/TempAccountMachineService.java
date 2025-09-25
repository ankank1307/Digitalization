package com.cncdigitalizationplatform.cncdigitalizationplatform.service;

public interface TempAccountMachineService {
    void addTempAccountMachine(String accountId, String machineId);
    void deleteTempAccountMachine(String tempAccountMachineId);
}
