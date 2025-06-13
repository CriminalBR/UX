package equipe.hackathon.util;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUploadUtil {
    
    // Diretório onde as imagens serão salvas
    private static final String UPLOAD_DIR = "uploads";
    
    /**
     * Abre um diálogo para seleção de arquivo e faz o upload para o diretório de uploads
     * @param parentComponent Componente pai para o diálogo de seleção de arquivo
     * @return URL relativa do arquivo salvo ou null em caso de erro
     */
    public static String uploadFile(Component parentComponent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione uma imagem");
        
        // Filtro para aceitar apenas arquivos de imagem
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String filename = f.getName().toLowerCase();
                return filename.endsWith(".jpg") || filename.endsWith(".jpeg") || 
                       filename.endsWith(".png") || filename.endsWith(".gif");
            }

            @Override
            public String getDescription() {
                return "Imagens (*.jpg, *.jpeg, *.png, *.gif)";
            }
        });
        
        int result = fileChooser.showOpenDialog(parentComponent);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Cria o diretório de uploads se não existir
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // Gera um nome único para o arquivo
                String originalFileName = selectedFile.getName();
                String fileExtension = "";
                int i = originalFileName.lastIndexOf('.');
                if (i > 0) {
                    fileExtension = originalFileName.substring(i);
                }
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                
                // Caminho de destino
                Path destination = Paths.get(UPLOAD_DIR, uniqueFileName);
                
                // Copia o arquivo para o diretório de uploads
                Files.copy(selectedFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
                
                // Retorna o caminho relativo do arquivo
                return UPLOAD_DIR + "/" + uniqueFileName;
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parentComponent, 
                    "Erro ao fazer upload do arquivo: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return null;
            }
        }
        
        return null;
    }
    
    /**
     * Remove um arquivo de upload
     * @param filePath Caminho do arquivo a ser removido
     * @return true se o arquivo foi removido com sucesso, false caso contrário
     */
    public static boolean removeFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        
        try {
            File file = new File(filePath);
            return file.exists() && file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
