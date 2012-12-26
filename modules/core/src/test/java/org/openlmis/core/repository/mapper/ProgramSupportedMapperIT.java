package org.openlmis.core.repository.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlmis.core.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.natpryce.makeiteasy.MakeItEasy.*;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.openlmis.core.builder.FacilityBuilder.defaultFacility;
import static org.openlmis.core.builder.ProgramBuilder.*;
import static org.openlmis.core.builder.ProgramSupportedBuilder.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext-core.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ProgramSupportedMapperIT {

  public static final String YELLOW_FEVER = "YELL_FVR";

  @Autowired
  ProgramMapper programMapper;

  @Autowired
  FacilityMapper facilityMapper;

  @Autowired
  ProgramSupportedMapper programSupportedMapper;

  @Autowired
  private RoleRightsMapper roleRightsMapper;

  @Autowired
  private RoleAssignmentMapper roleAssignmentMapper;

  @Autowired
  private UserMapper userMapper;

  @Test
  public void shouldSaveProgramSupported() throws Exception {
    Facility facility = make(a(defaultFacility));
    facilityMapper.insert(facility);
    Program program = make(a(defaultProgram, with(programCode, YELLOW_FEVER)));
    programMapper.insert(program);
    ProgramSupported programSupported = make(a(defaultProgramSupported,
        with(supportedFacilityId, facility.getId()),
        with(supportedProgramId, program.getId())));
    programSupportedMapper.addSupportedProgram(programSupported);

    List<ProgramSupported> programsSupported = programSupportedMapper.getBy(facility.getId(), program.getId());
    ProgramSupported result = programsSupported.get(0);
    assertThat(result.getFacilityId(), is(facility.getId()));
    assertThat(result.getProgramId(), is(program.getId()));
  }

  @Test
  public void shouldDeleteProgramMapping() throws Exception {
    Facility facility = make(a(defaultFacility));
    facilityMapper.insert(facility);
    Program program = make(a(defaultProgram, with(programCode, YELLOW_FEVER)));
    programMapper.insert(program);
    ProgramSupported programSupported = new ProgramSupported(facility.getId(), program.getId(), true, "user", now().toDate());
    programSupportedMapper.addSupportedProgram(programSupported);

    programSupportedMapper.delete(facility.getId(), program.getId());

    List<ProgramSupported> programsSupported = programSupportedMapper.getBy(facility.getId(), program.getId());
    assertFalse(programsSupported.contains(programSupported));
  }

  @Test
  public void shouldFetchActiveProgramsForGivenProgramIdsForAUserAndAFacility() {
    User user = insertUser();

    Program activeProgram = insertProgram(make(a(defaultProgram, with(programCode, "p1"))));
    Program inactiveProgram = insertProgram(make(a(defaultProgram, with(programCode, "p3"), with(programStatus, false))));

    Role r1 = new Role("r1", "random description");
    roleRightsMapper.insertRole(r1);
    roleRightsMapper.createRoleRight(r1.getId(), Right.CREATE_REQUISITION);
    insertRoleAssignments(activeProgram, user, r1);
    insertRoleAssignments(inactiveProgram, user, r1);

    Facility facility = insertFacility(make(a(defaultFacility)));

    insertProgramSupportedForFacility(activeProgram, facility, true);
    insertProgramSupportedForFacility(inactiveProgram, facility, true);

    ArrayList<Integer> programCodes = new ArrayList<>();
    programCodes.add(activeProgram.getId());
    programCodes.add(inactiveProgram.getId());

    String programCodesCommaSeparated = programCodes.toString().replace("[", "{").replace("]", "}");
    List<Program> programs = programSupportedMapper.filterActiveProgramsAndFacility(programCodesCommaSeparated, facility.getId());
    assertEquals(1, programs.size());
    assertEquals(activeProgram.getCode(), programs.get(0).getCode());
  }

  private void insertProgramSupportedForFacility(Program program, Facility facility, boolean isActive) {
    programSupportedMapper.addSupportedProgram(new ProgramSupported(facility.getId(), program.getId(), isActive, null, null));
  }

  private Program insertProgram(Program program) {
    programMapper.insert(program);
    return program;
  }

  private Facility insertFacility(Facility facility) {
    facilityMapper.insert(facility);
    return facility;
  }

  private Role insertRoleAssignments(Program program, User user, Role role) {
    roleAssignmentMapper.createRoleAssignment(user, role, program, null);
    return role;
  }

  private User insertUser() {
    User user = new User("random123123", "pwd");
    userMapper.insert(user);
    return user;
  }
}
