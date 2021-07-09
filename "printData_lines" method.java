public static void printData_lines(ResultSet resultSet0) {

    if (resultSet0 != null) {

        ColumnDefinitions colDefinitions1 = resultSet0.getColumnDefinitions();
        int numberOfColumns = colDefinitions1.size();
        List<Row> arrlistRows = resultSet0.all();
        int numberOfRows = arrlistRows.size();

        ArrayList<String> arrlistColumnName = new ArrayList<String>();
        ArrayList<String> arrlistColumnType = new ArrayList<String>();

        for (ColumnDefinition itemColDefinition : colDefinitions1) {
            arrlistColumnName.add(itemColDefinition.getName().toString());
            arrlistColumnType.add(itemColDefinition.getType().toString());

        }

        System.out.println(arrlistColumnName);
        System.out.println(arrlistColumnType);

        // Reading rows.
        String outputCell = "";
        String outputCellValue = "";
        for (int k1 = 0; k1 < numberOfRows; k1++) {

            for (int k2 = 0; k2 < numberOfColumns; k2++) {

                outputCellValue = "";
                outputCell = "";

                outputCell = arrlistColumnName.get(k2) + ": ";

                switch (arrlistColumnType.get(k2)) {
                    case "INT":
                        outputCellValue += arrlistRows.get(k1).getInt(arrlistColumnName.get(k2));
                        break;
                    case "DOUBLE":
                        outputCellValue += arrlistRows.get(k1).getDouble(arrlistColumnName.get(k2));
                        break;
                    case "TEXT":
                        outputCellValue += arrlistRows.get(k1).getString(arrlistColumnName.get(k2));
                        break;
                    case "VARINT":
                        outputCellValue += arrlistRows.get(k1).getBigInteger(arrlistColumnName.get(k2));
                        break;
                    case "BOOLEAN":
                        outputCellValue += arrlistRows.get(k1).getBoolean(arrlistColumnName.get(k2));
                        break;
                    case "VARCHAR":
                        outputCellValue += arrlistRows.get(k1).getString(arrlistColumnName.get(k2));
                        break;

                }
                outputCell += outputCellValue;
                System.out.println(outputCell);

            }
        }
    }

    else {
        System.out.println("Empty result set. ");
    }

}
