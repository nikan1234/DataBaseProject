package com.railway.ui.window.entrance.pages.drivers;

import com.railway.database.DatabaseController;
import com.railway.database.Matcher;
import com.railway.database.tables.employee.EmployeeDomains;
import com.railway.ui.window.common.entity.Driver;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.*;

public class DriversFormModel {

        private Table<?> sourceTable;
        private List<Field<?>> fields;

        public DriversFormModel() {
            sourceTable = table(EmployeeDomains.Driver.TABLE_NAME);

            fields = Arrays.asList(
                    field(EmployeeDomains.EMPLOYEE_ID),
                    field(EmployeeDomains.EMPLOYEE_LAST_NAME),
                    field(EmployeeDomains.EMPLOYEE_FIRST_NAME),
                    field(EmployeeDomains.EMPLOYEE_SECOND_NAME),
                    field(EmployeeDomains.EMPLOYEE_BIRTH_DATE),
                    field(EmployeeDomains.EMPLOYEE_HIRE_DATE),
                    field(EmployeeDomains.EMPLOYEE_SALARY),
                    field(EmployeeDomains.EMPLOYEE_GENDER),
                    field(EmployeeDomains.EMPLOYEE_CHILD_COUNT));
        }

        public List<Driver> getDriverList(Collection<Matcher> matchers) {
            try {
                List<Driver> drivers = new LinkedList<>();

                Collection<Condition> conditions = matchers.stream()
                        .map(s -> condition(s.getCondition()))
                        .collect(Collectors.toList());

                PreparedStatement statement = DatabaseController
                        .getInstance().getConnection().prepareStatement(selectDistinct(fields)
                                .from(sourceTable)
                                .where(conditions)
                                .orderBy(field(EmployeeDomains.EMPLOYEE_ID))
                                .toString().replace("\"", "")
                        );

                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    drivers.add(new Driver(
                            result.getInt(EmployeeDomains.EMPLOYEE_ID),
                            result.getString(EmployeeDomains.EMPLOYEE_LAST_NAME),
                            result.getString(EmployeeDomains.EMPLOYEE_FIRST_NAME),
                            result.getString(EmployeeDomains.EMPLOYEE_SECOND_NAME),
                            result.getDate(EmployeeDomains.EMPLOYEE_BIRTH_DATE).toLocalDate(),
                            result.getDate(EmployeeDomains.EMPLOYEE_HIRE_DATE).toLocalDate(),
                            result.getDouble(EmployeeDomains.EMPLOYEE_SALARY),
                            result.getString(EmployeeDomains.EMPLOYEE_GENDER),
                            result.getInt(EmployeeDomains.EMPLOYEE_CHILD_COUNT)
                    ));
                }
                return drivers;
            }
            catch (final SQLException e) {
                System.err.println(e.getMessage());
            }
            return new LinkedList<>();
        }

        public int getDriversCount(Collection<Matcher> matchers) {
            try {
                Collection<Condition> conditions = matchers.stream()
                        .map(s -> condition(s.getCondition()))
                        .collect(Collectors.toList());

                PreparedStatement statement = DatabaseController
                        .getInstance().getConnection().prepareStatement(
                                select(countDistinct(field(fields.get(0))))
                                        .from(sourceTable)
                                        .where(conditions)
                                        .toString()
                                        .replace("\"", "")
                        );
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    return result.getInt(1);
                }
            }
            catch (final SQLException e) {
                System.err.println(e.getMessage());
            }
            return 0;
        }
    }

